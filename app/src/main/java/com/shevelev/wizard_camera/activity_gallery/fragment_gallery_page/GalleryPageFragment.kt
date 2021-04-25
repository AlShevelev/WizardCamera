package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilter
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.NewspaperFilterSettings
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

        val photoSettings = requireArguments().getParcelable<PhotoShot>(ARG_PHOTO)!!

        loadPhoto(photoSettings)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        
        scopeJob.cancel()
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGalleryPageBinding =
        FragmentGalleryPageBinding.inflate(inflater, container, false)

    private fun loadPhoto(photoSettings: PhotoShot) {
        launch {
            val photoBitmap = withContext(dispatchersProvider.ioDispatcher) {
                BitmapFactory.decodeFile(filesHelper.getShotFileByName(photoSettings.fileName).absolutePath)
            }

            //GrayscaleSurfaceRenderer(requireContext(), photoBitmap)
            val filter =  GLSurfaceShaderFilter(
                requireContext(),
                photoBitmap,
                FiltersFactory.getFilterRes(photoSettings.filter.code),
                requireContext().getScreenSize(),
                FiltersFactory.createGLFilterSettings(photoSettings.filter, requireContext())
            )

            GLSurfaceViewBitmap.createAndAddToView(requireContext(), binding.imageContainer, photoBitmap, filter)
        }
    }

    companion object {
        private const val ARG_PHOTO = "PHOTO"

        fun newInstance(item: PhotoShot): GalleryPageFragment =
            GalleryPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PHOTO, item)
                }
            }
    }
}