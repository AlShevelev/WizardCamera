package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.catalano.impl.facade.ImageProcessorImpl
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditorStorageImpl
constructor(
    private val filesHelper: FilesHelper,
    private val photoShotRepository: PhotoShotRepository,
    private val fragmentsDataPass: FragmentsDataPass,
    private val filterSettings: FilterSettingsFacade,
    private val bitmapHelper: BitmapHelper
) : EditorStorage {
    override lateinit var displayedImage: Bitmap

    private lateinit var sourceImage: Bitmap

    private var histogramEqualizedImage: Bitmap? = null

    private val usedFilters = mutableMapOf<GlFilterCode, GlFilterSettings>()

    private lateinit var sourceShot: PhotoShot

    override var lastUsedGlFilter: GlFilterSettings? = null
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

    override suspend fun initImage(sourceShot: PhotoShot) {
        this.sourceShot = sourceShot

        memorizeUsedFilter(sourceShot.filter)
        lastUsedGlFilter = sourceShot.filter.takeIf { it.code != GlFilterCode.ORIGINAL }

        sourceImage = withContext(Dispatchers.IO) {
            BitmapFactory.decodeFile(filesHelper.getShotFileByName(sourceShot.fileName).absolutePath)
        }

        displayedImage = sourceImage
    }

    override fun switchToSourceImage() {
        displayedImage = sourceImage
    }

    override suspend fun switchToHistogramEqualizedImage() {
        if(histogramEqualizedImage == null) {
            histogramEqualizedImage = withContext(Dispatchers.Default) {
                ImageProcessorImpl().processHistogramEqualization(sourceImage)
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

        withContext(Dispatchers.IO) {
            // File with image
            bitmapHelper.save(filesHelper.getShotFileByName(sourceShot.fileName), displayedImage)

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