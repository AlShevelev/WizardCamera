package com.shevelev.wizard_camera.main_activity.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.camera.camera_manager.CameraXManager
import com.shevelev.wizard_camera.camera.camera_renderer.CameraRenderer
import com.shevelev.wizard_camera.camera.camera_renderer.GLRenderer
import com.shevelev.wizard_camera.camera.camera_settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.camera.filter.CameraFilter
import com.shevelev.wizard_camera.camera.filter.factory.FiltersFactory
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.EmptyFilterSettings
import com.shevelev.wizard_camera.databinding.ActivityMainBinding
import com.shevelev.wizard_camera.gallery_activity.view.GalleryActivity
import com.shevelev.wizard_camera.main_activity.di.MainActivityComponent
import com.shevelev.wizard_camera.main_activity.dto.*
import com.shevelev.wizard_camera.main_activity.view.gestures.Gesture
import com.shevelev.wizard_camera.main_activity.view.gestures.GesturesDetector
import com.shevelev.wizard_camera.main_activity.view_model.MainActivityViewModel
import com.shevelev.wizard_camera.shared.dialogs.OkDialog
import com.shevelev.wizard_camera.shared.mvvm.view.ActivityBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.shared.ui_utils.hideSystemUI
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject
import kotlin.system.exitProcess

@RuntimePermissions
class MainActivity : ActivityBaseMVVM<ActivityMainBinding, MainActivityViewModel>(), TextureView.SurfaceTextureListener {
    private lateinit var textureView: TextureView

    private var renderer: GLRenderer? = null

    private lateinit var cameraManager: CameraXManager

    // Temporary!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private var filerCode: FilterCode = FilterCode.NEGATIVE

//    private lateinit var gestureDetector: GesturesDetector

    @Inject
    internal lateinit var cameraSettingsRepository: CameraSettingsRepository

    override fun provideViewModelType(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun layoutResId(): Int = R.layout.activity_main

    override fun inject(key: String) = App.injections.get<MainActivityComponent>(key).inject(this)

    override fun releaseInjection(key: String) = App.injections.release<MainActivityComponent>(key)

    override fun linkViewModel(binding: ActivityMainBinding, viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        gestureDetector = GesturesDetector(this).apply { setOnGestureListener { processGesture(it) } }
//
//        viewModel.selectedFilter.observe(this, { renderer?.setFilter(it) })
//        viewModel.allFiltersListData.observe(this, { binding.allFiltersCarousel.setStartData(it, viewModel) })
//        viewModel.favoriteFiltersListData.observe(this, { binding.favoritesFiltersCarousel.setStartData(it, viewModel) })
//
//        binding.shootButton.setOnClickListener { textureView?.let { viewModel.onShootClick(it) } }
//        binding.flashButton.setOnClickListener { viewModel.onFlashClick() }
//        binding.filtersModeButton.setOnModeChangeListener { viewModel.onSwitchFilterModeClick(it) }
//        binding.autoFocusButton.setOnClickListener { viewModel.onAutoFocusClick() }
//        binding.expositionBar.setOnValueChangeListener { viewModel.onExposeValueUpdated(it) }
//        binding.galleryButton.setOnClickListener { viewModel.onGalleyClick() }
//
//        binding.allFiltersCarousel.setOnItemSelectedListener { viewModel.onFilterSelected(it) }
//        binding.favoritesFiltersCarousel.setOnItemSelectedListener { viewModel.onFavoriteFilterSelected(it) }
//
//        binding.settings.setOnSettingsChangeListener { viewModel.onFilterSettingsChange(it) }
//
//        binding.root.layoutTransition.setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())

        setupCameraWithPermissionCheck()
    }

    override fun onResume() {
        super.onResume()
//        viewModel.onActive()
    }

    override fun onPause() {
        super.onPause()
//        viewModel.onInactive()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
//            is SetupCameraCommand -> setupCameraWithPermissionCheck()
//            is ReleaseCameraCommand -> releaseCamera()
//            is SetFlashStateCommand -> renderer!!.updateFlashState(command.turnFlashOn)
//            is ShowCapturingSuccessCommand -> binding.captureSuccess.show(command.screenOrientation)
//            is FocusOnTouchCommand -> renderer!!.focusOnTouch(command.touchPoint, command.touchAreaSize)
//            is AutoFocusCommand -> renderer!!.setAutoFocus()
//            is ZoomCommand -> renderer!!.zoom(command.touchDistance).let { viewModel.onZoomUpdated(it) }
//            is ResetExposureCommand -> binding.expositionBar.reset()
//            is SetExposureCommand -> renderer!!.updateExposure(command.exposureValue)
//            is NavigateToGalleryCommand -> navigateToGallery()
//            is ExitCommand -> exit(command.messageResId)
//            is ShowFilterSettingsCommand -> {
//                binding.settings.hide()
//                binding.settings.show(command.settings)
//            }
//            is HideFilterSettingsCommand -> binding.settings.hide()
        }
    }

    override fun onBackPressed() {
        if(viewModel.onBackClick()) {
            super.onBackPressed()
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
    }

    @Suppress("MoveLambdaOutsideParentheses")
    @SuppressLint("ClickableViewAccessibility")
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun setupCamera() {
//        if(!viewModel.isActive) {
//            return
//        }

        textureView = TextureView(this).also {
            binding.root.addView(it)
            it.surfaceTextureListener = this
        }

//        renderer = CameraRenderer(
//            this,
//            viewModel.isFlashActive,
//            viewModel.isAutoFocus,
//            viewModel.cameraSettings,
//            { viewModel.onCameraIsSetUp() }).also {
//                textureView = TextureView(this)
//                binding.root.addView(textureView, 0)
//                textureView!!.surfaceTextureListener = it
//
//                with(viewModel.cameraSettings.screenTextureSize) {
//                    textureView!!.layoutParams = ConstraintLayout.LayoutParams(width, height)
//                }
//
//                textureView!!.setOnTouchListener { view, event ->
//                    gestureDetector.onTouchEvent(view, event)
//                    true
//                }
//                it.setSelectedFilter(viewModel.selectedFilter.value!!)
//        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    internal fun onCameraPermissionsDenied() = viewModel.onPermissionDenied()

    @SuppressLint("ClickableViewAccessibility")
    private fun releaseCamera() {
//        binding.root.removeView(textureView)
//        renderer = null
//
//        textureView?.setOnTouchListener(null)
//        textureView?.addOnLayoutChangeListener(null)
//
//        textureView = null
    }

    private fun startRendering(surface: SurfaceTexture, width: Int, height: Int) {
        if(renderer != null) {
            return
        }

        renderer = GLRenderer(-width, -height).also {
            it.initGL(surface)

            val filtersFactory = FiltersFactory(this)
            val selectedFilterSettings = EmptyFilterSettings(filerCode)
            val selectedFilter = filtersFactory.getFilter(selectedFilterSettings.code)
            selectedFilter.onAttach(selectedFilterSettings)

            it.setFilter(selectedFilter)

            cameraManager = CameraXManager(cameraSettingsRepository)
            cameraManager.initCamera(this, this, it.cameraSurfaceTexture, textureView, CameraSelector.LENS_FACING_BACK)
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

    private fun processGesture(gesture: Gesture) = viewModel.processGesture(gesture)

    private fun navigateToGallery() {
        val galleryIntent = Intent(this, GalleryActivity::class.java)
        startActivity(galleryIntent)
    }

    private fun exit(@StringRes messageResId: Int) {
        OkDialog.show(supportFragmentManager, messageResId) {
            exitProcess(0)
        }
    }
}