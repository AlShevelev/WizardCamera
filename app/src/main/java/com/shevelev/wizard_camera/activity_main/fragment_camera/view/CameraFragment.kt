package com.shevelev.wizard_camera.activity_main.fragment_camera.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.view.View
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.GalleryActivity
import com.shevelev.wizard_camera.activity_main.fragment_camera.di.CameraFragmentComponent
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.*
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures.GesturesDetector
import com.shevelev.wizard_camera.activity_main.fragment_camera.view_model.CameraFragmentViewModel
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.camera.camera_manager.CameraManager
import com.shevelev.wizard_camera.camera.camera_renderer.GLRenderer
import com.shevelev.wizard_camera.camera.camera_settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.camera.filter.CameraFilter
import com.shevelev.wizard_camera.camera.filter.factory.FiltersFactory
import com.shevelev.wizard_camera.databinding.FragmentCameraBinding
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

@RuntimePermissions
class CameraFragment : FragmentBaseMVVM<FragmentCameraBinding, CameraFragmentViewModel>(), TextureView.SurfaceTextureListener {
    private lateinit var textureView: TextureView

    private var renderer: GLRenderer? = null

    private lateinit var cameraManager: CameraManager

    private lateinit var gestureDetector: GesturesDetector

    @Inject
    internal lateinit var cameraSettingsRepository: CameraSettingsRepository

    override fun provideViewModelType(): Class<CameraFragmentViewModel> = CameraFragmentViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_camera

    override fun linkViewModel(binding: FragmentCameraBinding, viewModel: CameraFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun inject() = App.injections.get<CameraFragmentComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<CameraFragmentComponent>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gestureDetector = GesturesDetector(requireContext()).apply { setOnGestureListener { viewModel.processGesture(it) } }

        viewModel.selectedFilter.observe(
            {viewLifecycleOwner.lifecycle},
            { renderer?.setFilter(it) })

        viewModel.allFiltersListData.observe(
            {viewLifecycleOwner.lifecycle},
            { binding.allFiltersCarousel.setStartData(it, viewModel) })

        viewModel.favoriteFiltersListData.observe(
            {viewLifecycleOwner.lifecycle},
            { binding.favoritesFiltersCarousel.setStartData(it, viewModel) })

        binding.shootButton.setOnClickListener { textureView.let { viewModel.onCaptureClick() } }
        binding.flashButton.setOnClickListener { viewModel.onFlashClick() }
        binding.filtersModeButton.setOnModeChangeListener { viewModel.onSwitchFilterModeClick(it) }
        binding.expositionBar.setOnValueChangeListener { viewModel.onExposeValueUpdated(it) }
        binding.galleryButton.setOnClickListener { viewModel.onGalleyClick() }

        binding.allFiltersCarousel.setOnItemSelectedListener { viewModel.onFilterSelected(it) }
        binding.favoritesFiltersCarousel.setOnItemSelectedListener { viewModel.onFavoriteFilterSelected(it) }

        binding.settings.setOnSettingsChangeListener { viewModel.onFilterSettingsChange(it) }

        binding.root.layoutTransition.setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())

        setupCameraWithPermissionCheck()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onActive()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onInactive()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
            is ShowCapturingSuccessCommand -> binding.captureSuccess.show(command.screenOrientation)
            is ZoomCommand -> cameraManager.zoom(command.scaleFactor).let { viewModel.onZoomUpdated(it) }
            is ResetExposureCommand -> binding.expositionBar.reset()
            is SetExposureCommand -> cameraManager.updateExposure(command.exposureValue)
            is NavigateToGalleryCommand -> navigateToGallery()
            is ShowFilterSettingsCommand -> {
                binding.settings.hide()
                binding.settings.show(command.settings)
            }

            is HideFilterSettingsCommand -> binding.settings.hide()

            is StartCaptureCommand -> cameraManager.capture(command.targetFile, command.isFlashLightActive) { isSuccess ->
                viewModel.onCaptureComplete(isSuccess)
            }
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        binding.root.postDelayed({ startRendering(surface, width, height) }, 500)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        startRendering(surface, width, height)    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        stopRendering()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        // do nothing
    }

    @SuppressLint("ClickableViewAccessibility")
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun setupCamera() {
        textureView = TextureView(requireContext()).also {
            binding.textureContainer.addView(it)
            it.surfaceTextureListener = this

            it.setOnTouchListener { view, event ->
                gestureDetector.onTouchEvent(view, event)
                true
            }
        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    internal fun onCameraPermissionsDenied() = viewModel.onPermissionDenied()

    private fun startRendering(surface: SurfaceTexture, width: Int, height: Int) {
        if(renderer != null) {
            return
        }

        renderer = GLRenderer(-width, -height, FiltersFactory(requireContext().applicationContext)).also {
            it.initGL(surface)

            it.setFilter(viewModel.selectedFilter.value!!)

            cameraManager = CameraManager(cameraSettingsRepository)
            cameraManager.initCamera(requireContext(), this, it.cameraSurfaceTexture, textureView) {
                binding.root.post {
                    viewModel.onCameraIsSetUp(cameraManager.isFlashLightSupported)
                }
            }
        }
    }

    private fun stopRendering() {
        if(renderer == null) {
            return
        }

        renderer?.releaseGL()
        renderer = null

        cameraManager.releaseCamera()

        CameraFilter.release()
    }

    private fun navigateToGallery() {
        val galleryIntent = Intent(requireContext(), GalleryActivity::class.java)
        startActivity(galleryIntent)
    }
}