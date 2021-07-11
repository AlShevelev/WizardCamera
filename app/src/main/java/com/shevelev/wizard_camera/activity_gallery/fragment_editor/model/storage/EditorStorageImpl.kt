package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shevelev.catalano.fast_bitmap.FastBitmap
import com.shevelev.catalano.filters.HistogramEqualization
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
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
    private val filesHelper: FilesHelper
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
}