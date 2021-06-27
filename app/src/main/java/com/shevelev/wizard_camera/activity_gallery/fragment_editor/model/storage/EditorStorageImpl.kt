package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
    private val filesHelper: FilesHelper,
) : EditorStorage {

    private val usedFilters = mutableMapOf<GlFilterCode, GlFilterSettings>()

    override lateinit var image: Bitmap

    override var lastUsedGlFilter: GlFilterSettings? = sourceShot.filter.takeIf { it.code != GlFilterCode.ORIGINAL }
        set(value) {
            value?.let {
                field = it
                memorizeUsedFilter(it)
            }
        }

    init {
        memorizeUsedFilter(sourceShot.filter)
    }

    override suspend fun decodeBitmap() {
        image = withContext(dispatchersProvider.ioDispatcher) {
            BitmapFactory.decodeFile(filesHelper.getShotFileByName(sourceShot.fileName).absolutePath)
        }
    }

    override fun getUsedFilter(code: GlFilterCode): GlFilterSettings? = usedFilters[code]

    override fun memorizeUsedFilter(filter: GlFilterSettings) {
        usedFilters[filter.code] = filter
    }
}