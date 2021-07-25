package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shevelev.catalano.fast_bitmap.FastBitmap
import com.shevelev.catalano.filters.HistogramEqualization
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.storage.repositories.PhotoShotRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @property sourceShot an initial shot
 */
class EditorStorageImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val sourceShot: PhotoShot,
    private val filesHelper: FilesHelper,
    private val photoShotRepository: PhotoShotRepository,
    private val fragmentsDataPass: FragmentsDataPass,
    private val filterSettings: FilterSettingsFacade
) : EditorStorage {
    override lateinit var displayedImage: Bitmap

    private lateinit var sourceImage: Bitmap

    private var histogramEqualizedImage: Bitmap? = null

    private val usedFilters = mutableMapOf<GlFilterCode, GlFilterSettings>()

    override var lastUsedGlFilter: GlFilterSettings? = sourceShot.filter.takeIf { it.code != GlFilterCode.ORIGINAL }
        set(value) {
            value?.let {
                field = it
                memorizeUsedFilter(it)
            }
        }

    override val isSourceImageDisplayed: Boolean
        get() = displayedImage == sourceImage

    override var isUpdated: Boolean = false
        private set

    override var isInNoFiltersMode: Boolean = false

    init {
        memorizeUsedFilter(sourceShot.filter)
    }

    override suspend fun initImage() {
        sourceImage = withContext(dispatchersProvider.ioDispatcher) {
            BitmapFactory.decodeFile(filesHelper.getShotFileByName(sourceShot.fileName).absolutePath)
        }

        displayedImage = sourceImage
    }

    override fun switchToSourceImage() {
        displayedImage = sourceImage
    }

    override suspend fun switchToHistogramEqualizedImage() {
        if(histogramEqualizedImage == null) {
            histogramEqualizedImage = withContext(dispatchersProvider.calculationsDispatcher) {
                val sourceCopy = sourceImage.copy(sourceImage.config, true)
                val fastBitmap = FastBitmap(sourceCopy)

                val histogramEqualization = HistogramEqualization()
                histogramEqualization.applyInPlace(fastBitmap)

                fastBitmap.toBitmap()
            }
        }

        displayedImage = histogramEqualizedImage!!
    }

    override fun getUsedFilter(code: GlFilterCode): GlFilterSettings? = usedFilters[code]

    override fun memorizeUsedFilter(filter: GlFilterSettings) {
        usedFilters[filter.code] = filter
    }

    override fun onUpdate() {
        isUpdated = true
    }

    /**
     * Saves result of the editing
     */
    override suspend fun saveResult() {
        if(!isUpdated) {
            return
        }

        withContext(dispatchersProvider.ioDispatcher) {
            // File with image
            // Jpeg format with "95" quality value is used - as same as ImageCapture settings
            // See [CameraManager.bindCameraUseCases]
            filesHelper.getShotFileByName(sourceShot.fileName).outputStream().use {
                displayedImage.compress(Bitmap.CompressFormat.JPEG, 95, it)
            }

            // Metadata in the database
            val filter = if(isInNoFiltersMode) {
                filterSettings[GlFilterCode.ORIGINAL]
            } else {
                lastUsedGlFilter ?: sourceShot.filter
            }

            val shotToSave = sourceShot.copy(filter = filter)
            photoShotRepository.update(shotToSave)

            fragmentsDataPass.putPhotoShot(shotToSave)
        }
    }
}