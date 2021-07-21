package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilter
import com.shevelev.wizard_camera.databinding.FragmentGalleryPageBinding
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.factory.FiltersFactory
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBase
import com.shevelev.wizard_camera.utils.resources.getScreenSize
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class GalleryPageFragment : FragmentBase<FragmentGalleryPageBinding>(), CoroutineScope {
    private lateinit var scopeJob: Job

    private val errorHandler = CoroutineExceptionHandler { _, exception -> Timber.e(exception) }

    /**
     * Context of this scope.
     */
    override lateinit var coroutineContext: CoroutineContext

    @Inject
    internal lateinit var filesHelper: FilesHelper

    @Inject
    internal lateinit var dispatchersProvider: DispatchersProvider

    override fun inject() = App.injections.get<GalleryPageFragmentComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<GalleryPageFragmentComponent>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scopeJob = SupervisorJob()
        coroutineContext = scopeJob + dispatchersProvider.uiDispatcher + errorHandler

        val item = requireArguments().getParcelable<GalleryItem>(ARG_PHOTO)!!

        loadPhoto(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        
        scopeJob.cancel()
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGalleryPageBinding =
        FragmentGalleryPageBinding.inflate(inflater, container, false)

    private fun loadPhoto(item: GalleryItem) {
        launch {
            val photoBitmap = withContext(dispatchersProvider.ioDispatcher) {
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

            GLSurfaceViewBitmap.createAndAddToView(requireContext(), binding.imageContainer, photoBitmap, filter)
        }
    }

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