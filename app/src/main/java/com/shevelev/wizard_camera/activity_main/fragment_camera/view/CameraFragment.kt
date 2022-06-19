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
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.*
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures.GesturesDetector
import com.shevelev.wizard_camera.activity_main.fragment_camera.view_model.CameraFragmentViewModel
import com.shevelev.wizard_camera.core.camera_gl.api.camera.manager.CameraManager
import com.shevelev.wizard_camera.core.camera_gl.api.camera.renderer.GlRenderer
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.databinding.FragmentCameraBinding
import com.shevelev.wizard_camera.feature.filters_facade.api.di.FiltersFacadeInjectionSettings
import com.shevelev.wizard_camera.feature.filters_facade.impl.di.FiltersFacadeScope
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

private const val FILTERS_SCOPE_ID = "CAMERA_FRAGMENT_FILTERS_SCOPE_ID"

@RuntimePermissions
class CameraFragment : FragmentBaseMVVM<FragmentCameraBinding, CameraFragmentViewModel>(), TextureView.SurfaceTextureListener {
    private lateinit var textureView: TextureView

    private var renderer: GlRenderer? = null

    private lateinit var cameraManager: CameraManager

    private lateinit var gestureDetector: GesturesDetector

    override val viewModel: CameraFragmentViewModel by viewModel{
        parametersOf(
            FiltersFacadeInjectionSettings(
                FILTERS_SCOPE_ID,
                useInMemoryLastUsedFilters = false,
                canUpdateFavorites = true
            )
        )
    }

    override fun layoutResId(): Int = R.layout.fragment_camera

    override fun linkViewModel(binding: FragmentCameraBinding, viewModel: CameraFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gestureDetector = GesturesDetector(requireContext()).apply { setOnGestureListener { viewModel.processGesture(it) } }

        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            renderer?.setFilter(it)
        }

        viewModel.filtersListData.observe(viewLifecycleOwner) {
            binding.filtersCarousel.updateData(it, viewModel)
        }

        viewModel.flowerFilters.observe(viewLifecycleOwner) {
            binding.flowerMenu.init(it)
        }

        binding.shootButton.setOnClickListener { textureView.let { viewModel.onCaptureClick() }  }
        binding.flashButton.setOnClickListener { viewModel.onFlashClick() }
        binding.expositionBar.setOnValueChangeListener { viewModel.onExposeValueUpdated(it) }
        binding.galleryButton.setOnClickListener { viewModel.onGalleyClick() }
        binding.filtersButton.setOnClickListener { viewModel.onFiltersMenuClick() }
        binding.flowerMenu.setOnItemClickListener { viewModel.onFilterFromMenuClick(it) }

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

    override fun onDestroy() {
        super.onDestroy()
        FiltersFacadeScope.close(FILTERS_SCOPE_ID)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
            is ShowCapturingSuccessCommand -> {
                binding.captureSuccess.show(command.screenOrientation)
            }
            is ZoomCommand -> cameraManager.zoom(command.scaleFactor).let { viewModel.onZoomUpdated(it) }
            ResetExposureCommand -> binding.expositionBar.reset()
            is SetExposureCommand -> cameraManager.updateExposure(command.exposureValue)
            NavigateToGalleryCommand -> navigateToGallery()
            is ShowFilterSettingsCommand -> {
                binding.settings.hide()
                binding.settings.show(command.settings)
            }

            is HideFilterSettingsCommand -> binding.settings.hide()

            is StartCaptureCommand -> cameraManager.capture(
                command.targetStream,
                command.isFlashLightActive,
                command.rotation
            ) { isSuccess ->
                viewModel.onCaptureComplete(isSuccess)
            }

            is SetFlowerMenuVisibilityCommand -> {
                if(command.isVisible) {
                    binding.flowerMenu.show()
                } else {
                    binding.flowerMenu.hide()
                }
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

        renderer = getKoin().get<GlRenderer>().also {
            it.initGL(surface, -width, -height)

            it.setFilter(viewModel.selectedFilter.value!!)

            cameraManager = getKoin().get()
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
    }

    private fun navigateToGallery() {
        val galleryIntent = Intent(requireContext(), GalleryActivity::class.java)
        startActivity(galleryIntent)
    }
}