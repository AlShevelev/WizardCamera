package com.shevelev.wizard_camera

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shevelev.wizard_camera.gl.CameraRenderer
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
import kotlin.math.abs
import kotlin.system.exitProcess

@RuntimePermissions
class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var container: FrameLayout
    private lateinit var renderer: CameraRenderer
    private lateinit var textureView: TextureView

    private var filterId = R.id.filterOriginal
    private var currentFilterId = 0

    private val titles = listOf(
        R.string.filterOriginal, R.string.filterEdgeDectection, R.string.filterPixelize, R.string.filterEMInterference, R.string.filterTrianglesMosaic,
        R.string.filterLegofied, R.string.filterTileMosaic, R.string.filterBlueorange, R.string.filterChromaticAberration, R.string.filterBasicDeform,
        R.string.filterContrast, R.string.filterNoiseWarp, R.string.filterRefraction, R.string.filterMapping, R.string.filterCrosshatch,
        R.string.filterLichtensteinEsque, R.string.filterAsciiArt, R.string.filterMoney, R.string.filterCracked, R.string.filterPolygonization,
        R.string.filterBlackAndWhite, R.string.filterGray, R.string.filterNegative, R.string.filterNostalgia, R.string.filterCasting,
        R.string.filterRelief, R.string.filterSwirl, R.string.filterHexagonMosaic, R.string.filterMirror, R.string.filterTriple,
        R.string.filterCartoon, R.string.filterWaterReflection
    )

    private val filterResIds = listOf(
        R.id.filterOriginal, R.id.filterEdgeDectection, R.id.filterPixelize, R.id.filterEMInterference, R.id.filterTrianglesMosaic,
        R.id.filterLegofied, R.id.filterTileMosaic, R.id.filterBlueorange, R.id.filterChromaticAberration, R.id.filterBasicDeform,
        R.id.filterContrast, R.id.filterNoiseWarp, R.id.filterRefraction, R.id.filterMapping, R.id.filterCrosshatch,
        R.id.filterLichtensteinEsque, R.id.filterAsciiArt, R.id.filterMoney, R.id.filterCracked, R.id.filterPolygonization,
        R.id.filterBlackAndWhite, R.id.filterGray, R.id.filterNegative, R.id.filterNostalgia, R.id.filterCasting,
        R.id.filterRelief, R.id.filterSwirl, R.id.filterHexagonMosaic, R.id.filterMirror, R.id.filterTriple,
        R.id.filterCartoon, R.id.filterWaterReflection
    )

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        container = FrameLayout(this)
        setContentView(container)

        setTitle(titles[currentFilterId])

        setupCameraPreviewViewWithPermissionCheck()

        gestureDetector = GestureDetector(this, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        filterId = item.itemId

        if (filterId == R.id.capture) {
            captureWithPermissionCheck()
            return true
        }

        title = item.title

        renderer.setSelectedFilter(filterId)

        currentFilterId = filterResIds.indexOf(filterId)
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        // do nothing
    }

    override fun onSingleTapUp(e: MotionEvent?) = false

    override fun onDown(e: MotionEvent?) = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        val velocity = if(abs(velocityX) > abs(velocityY)) velocityX else velocityY
        val step = if(velocity > 0) -1 else 1
        
        currentFilterId = circleLoop(titles.size, currentFilterId, step)
        setTitle(titles[currentFilterId])
        
        renderer.setSelectedFilter(filterResIds[currentFilterId])
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

    override fun onLongPress(e: MotionEvent?) {
        // do nothing
    }

    @SuppressLint("ClickableViewAccessibility")
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun setupCameraPreviewView() {
        renderer = CameraRenderer(this)
        textureView = TextureView(this)
        container.addView(textureView)
        textureView.surfaceTextureListener = renderer

        textureView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        textureView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            renderer.onSurfaceTextureSizeChanged(null, v.width, v.height)
        }
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    internal fun onCameraPermissionsDenied() {
        Toast.makeText(this, R.string.needCameraPermissionExit, Toast.LENGTH_LONG).show()
        container.postDelayed({ exitProcess(0) }, 3500)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun capture() {
        val imageFile = genSaveFileName(title.toString() + "_")
        Timber.d("imageFile: ${imageFile.absolutePath}")
        if (imageFile.exists()) {
            imageFile.delete()
        }

        // create bitmap screen capture
        val bitmap = textureView.bitmap

        try {
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                outputStream.flush()
                Toast.makeText(this, R.string.photoSaved, Toast.LENGTH_SHORT).show()
            }
        } catch (ex: FileNotFoundException) {
            Timber.e(ex)
            ex.printStackTrace()
            Toast.makeText(this, R.string.commonGeneralError, Toast.LENGTH_LONG).show()
        } catch (ex: IOException) {
            Timber.e(ex)
            ex.printStackTrace()
            Toast.makeText(this, R.string.commonGeneralError, Toast.LENGTH_LONG).show()
        }
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun onWritePermissionsDenied() {
        Toast.makeText(this, R.string.needWritePermission, Toast.LENGTH_LONG).show()
    }

    @Suppress("DEPRECATION")
    private fun genSaveFileName(prefix: String): File {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.US)
        val timeString = dateFormat.format(date)
        val externalPath = getExternalFilesDir(null)
        return File(externalPath, "$prefix$timeString.png")
    }

    private fun circleLoop(size: Int, currentPos: Int, step: Int): Int =
        when {
            step == 0 -> currentPos
            step > 0 -> {
                if (currentPos + step >= size) {
                    (currentPos + step) % size
                } else {
                    currentPos + step
                }
            }
            else -> {
                if (currentPos + step < 0) {
                    currentPos + step + size
                } else {
                    currentPos + step
                }
            }
        }
}