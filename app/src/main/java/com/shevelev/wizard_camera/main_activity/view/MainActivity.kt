package com.shevelev.wizard_camera.main_activity.view

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.TextureView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.camera.camera_renderer.CameraRenderer
import com.shevelev.wizard_camera.databinding.ActivityMainBinding
import com.shevelev.wizard_camera.main_activity.di.MainActivityComponent
import com.shevelev.wizard_camera.main_activity.dto.CaptureImageCommand
import com.shevelev.wizard_camera.main_activity.dto.ReleaseCameraCommand
import com.shevelev.wizard_camera.main_activity.dto.SetupCameraCommand
import com.shevelev.wizard_camera.main_activity.view.gestures.Gesture
import com.shevelev.wizard_camera.main_activity.view.gestures.GesturesDetector
import com.shevelev.wizard_camera.main_activity.view_model.MainActivityViewModel
import com.shevelev.wizard_camera.shared.animation.AnimationUtils
import com.shevelev.wizard_camera.shared.mvvm.view.ActivityBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.shared.ui_utils.hideSystemUI
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

@RuntimePermissions
class MainActivity : ActivityBaseMVVM<ActivityMainBinding, MainActivityViewModel>() {
    private var renderer: CameraRenderer? = null
    private var textureView: TextureView? = null

    private lateinit var gestureDetector: GesturesDetector

    private var titleAnimator: ValueAnimator? = null

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

        shootButton.setOnClickListener { capture() }

        viewModel.selectedFilter.observe(this, Observer { renderer?.setSelectedFilter(it) })
        viewModel.selectedFilterTitle.observe(this, Observer { updateTitle(it) })
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
            is CaptureImageCommand -> capture()
            is SetupCameraCommand -> setupCameraWithPermissionCheck()
            is ReleaseCameraCommand -> releaseCamera()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun setupCamera() {
        renderer = CameraRenderer(this).also {
            textureView = TextureView(this)
            root.addView(textureView, 0)
            textureView!!.surfaceTextureListener = it

            textureView!!.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
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

    private fun capture() {
        val imageFile = genSaveFileName(title.toString() + "_")
        Timber.d("imageFile: ${imageFile.absolutePath}")

        // create bitmap screen capture
        val bitmap = textureView!!.bitmap

        try {
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.WEBP, 75, outputStream)
                outputStream.flush()
            }

            MediaScannerConnection.scanFile(this, arrayOf<String>(imageFile.absolutePath), arrayOf<String>("image/webp"), null)

            Toast.makeText(this, R.string.photoSaved, Toast.LENGTH_SHORT).show()
        } catch (ex: FileNotFoundException) {
            Timber.e(ex)
            Toast.makeText(this, R.string.commonGeneralError, Toast.LENGTH_LONG).show()
        } catch (ex: IOException) {
            Timber.e(ex)
            Toast.makeText(this, R.string.commonGeneralError, Toast.LENGTH_LONG).show()
        }
    }

    private fun genSaveFileName(prefix: String): File {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.US)
        val timeString = dateFormat.format(date)

        val externalPath = externalMediaDirs[0]

        val dir = File(externalPath, getString(R.string.appName))
        if(!dir.exists()) {
            dir.mkdir()
        }

        return File(dir, "$prefix$timeString.webp")
    }

    private fun processGesture(gesture: Gesture) = viewModel.processGesture(gesture)

    private fun updateTitle(@StringRes titleResId: Int) {
        titleAnimator?.cancel()

        filterTitle.setText(titleResId)
        filterTitle.alpha = 1f

        titleAnimator = AnimationUtils.getFloatAnimator(
            duration = 500,
            forward = false,
            updateListener = { alpha -> filterTitle.alpha = alpha }
        ).apply {
            startDelay = 2000L
            start()
        }
    }
}