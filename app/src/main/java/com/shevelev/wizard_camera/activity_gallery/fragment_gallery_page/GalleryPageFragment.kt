package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.core.camera_gl.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.core.camera_gl.bitmap.filters.GLSurfaceShaderFilter
import com.shevelev.wizard_camera.databinding.FragmentGalleryPageBinding
import com.shevelev.wizard_camera.core.camera_gl.shared.factory.FiltersFactory
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import com.shevelev.wizard_camera.core.utils.resources.getScreenSize
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GalleryPageFragment : com.shevelev.wizard_camera.core.ui_utils.mvvm.view.FragmentBase<FragmentGalleryPageBinding>(), CoroutineScope {
    private lateinit var scopeJob: Job

    private val errorHandler = CoroutineExceptionHandler { _, exception -> Timber.e(exception) }

    private lateinit var surfaceView: GLSurfaceViewBitmap

    /**
     * Context of this scope.
     */
    override lateinit var coroutineContext: CoroutineContext

    @Inject
    internal lateinit var filesHelper: FilesHelper

    override fun inject() = App.injections.get<GalleryPageFragmentComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<GalleryPageFragmentComponent>()

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
                BitmapFactory.decodeFile(filesHelper.getShotFileByName(item.item.fileName).absolutePath)
            }

            //GrayscaleSurfaceRenderer(requireContext(), photoBitmap)
            val filter =  GLSurfaceShaderFilter(
                requireContext(),
                photoBitmap,
                FiltersFactory.getFilterRes(item.item.filter.code),
                requireContext().getScreenSize(),
                FiltersFactory.createGLFilterSettings(item.item.filter, requireContext())
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