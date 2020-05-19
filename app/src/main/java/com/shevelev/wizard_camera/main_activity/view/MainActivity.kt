package com.shevelev.wizard_camera.main_activity.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.TextureView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.camera.camera_renderer.CameraRenderer
import com.shevelev.wizard_camera.databinding.ActivityMainBinding
import com.shevelev.wizard_camera.main_activity.di.MainActivityComponent
import com.shevelev.wizard_camera.main_activity.dto.*
import com.shevelev.wizard_camera.main_activity.view.gestures.Gesture
import com.shevelev.wizard_camera.main_activity.view.gestures.GesturesDetector
import com.shevelev.wizard_camera.main_activity.view_model.MainActivityViewModel
import com.shevelev.wizard_camera.shared.mvvm.view.ActivityBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.shared.ui_utils.hideSystemUI
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import kotlin.system.exitProcess

@RuntimePermissions
class MainActivity : ActivityBaseMVVM<ActivityMainBinding, MainActivityViewModel>() {
    private var renderer: CameraRenderer? = null
    private var textureView: TextureView? = null

    private lateinit var gestureDetector: GesturesDetector

    override fun provideViewModelType(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun layoutResId(): Int = R.layout.activity_main

    override fun inject(key: String) = App.injections.get<MainActivityComponent>(key).inject(this)

    override fun releaseInjection(key: String) = App.injections.release<MainActivityComponent>(key)

    override fun linkViewModel(binding: ActivityMainBinding, viewModel: MainActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gestureDetector = GesturesDetector(this).apply { setOnGestureListener { processGesture(it) } }

        viewModel.selectedFilter.observe(this, Observer { renderer?.setSelectedFilter(it) })

        shootButton.setOnClickListener { textureView?.let { viewModel.onShootClick(it) } }
        flashButton.setOnClickListener { viewModel.onFlashClick() }
        turnFiltersButton.setOnClickListener { viewModel.onTurnFiltersClick() }
        autoFocusButton.setOnClickListener { viewModel.onAutoFocusClick() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onActive()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onInactive()
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
            is SetupCameraCommand -> setupCameraWithPermissionCheck()
            is ReleaseCameraCommand -> releaseCamera()
            is SetFlashStateCommand -> renderer!!.updateFlashState(command.turnFlashOn)
            is ShowCapturingSuccessCommand -> captureSuccess.show(command.screenOrientation)
            is FocusOnTouchCommand -> renderer!!.focusOnTouch(command.touchPoint, command.touchAreaSize)
            is AutoFocusCommand -> renderer!!.setAutoFocus()
            is ZoomCommand -> renderer!!.zoom(command.touchDistance).let { viewModel.onZoomUpdated(it) }
        }
    }

    @Suppress("MoveLambdaOutsideParentheses")
    @SuppressLint("ClickableViewAccessibility")
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun setupCamera() {
        renderer = CameraRenderer(this, viewModel.isFlashActive, viewModel.isAutoFocus, { viewModel.onCameraIsSetUp() }).also {
            textureView = TextureView(this)
            root.addView(textureView, 0)
            textureView!!.surfaceTextureListener = it

            textureView!!.setOnTouchListener { view, event ->
                gestureDetector.onTouchEvent(view, event)
                true
            }
            it.setSelectedFilter(viewModel.selectedFilter.value!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun releaseCamera() {
        root.removeView(textureView)
        renderer = null

        textureView!!.setOnTouchListener(null)
        textureView!!.addOnLayoutChangeListener(null)

        textureView = null
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    internal fun onCameraPermissionsDenied() {
        Toast.makeText(this, R.string.needCameraPermissionExit, Toast.LENGTH_LONG).show()
        root.postDelayed({ exitProcess(0) }, 3500)
    }

    private fun processGesture(gesture: Gesture) = viewModel.processGesture(gesture)
}