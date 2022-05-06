package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.core.camera_gl.api.shared.factory.GlShaderFiltersFactory
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.FragmentBase
import com.shevelev.wizard_camera.databinding.FragmentGalleryPageBinding
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class GalleryPageFragment : FragmentBase<FragmentGalleryPageBinding>(), CoroutineScope {
    private lateinit var scopeJob: Job

    private val errorHandler = CoroutineExceptionHandler { _, exception -> Timber.e(exception) }

    private lateinit var surfaceView: GLSurfaceViewBitmap

    private val filtersFactory: GlShaderFiltersFactory by inject()

    private val bitmapHelper: BitmapHelper by inject()

    /**
     * Context of this scope.
     */
    override lateinit var coroutineContext: CoroutineContext

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scopeJob = SupervisorJob()
        coroutineContext = scopeJob + Dispatchers.Main + errorHandler

        val item = requireArguments().getParcelable<GalleryItem>(ARG_PHOTO)!!

        loadPhoto(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        
        scopeJob.cancel()
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGalleryPageBinding =
        FragmentGalleryPageBinding.inflate(inflater, container, false)

    fun loadPhoto(item: GalleryItem) {
        launch {
            val photoBitmap = withContext(Dispatchers.IO) {
                bitmapHelper.load(item.item.contentUri)
            }

            val filter = filtersFactory.createFilter(
                bitmap = photoBitmap,
                settings = item.item.filter
            )

            surfaceView = GLSurfaceViewBitmap.createAndAddToView(
                requireContext(),
                binding.imageContainer,
                photoBitmap,
                filter)
        }
    }

    fun getBitmap(callback: (Bitmap?) -> Unit) = surfaceView.getBitmap(callback)

    companion object {
        private const val ARG_PHOTO = "PHOTO"

        fun newInstance(item: GalleryItem): GalleryPageFragment =
            GalleryPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PHOTO, item)
                }
            }
    }
}