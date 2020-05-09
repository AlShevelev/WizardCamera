package com.shevelev.wizard_camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shevelev.wizard_camera.gl.CameraRenderer
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private companion object {
        const val REQUEST_CAMERA_PERMISSION = 101
    }

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Camera access is required.", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, Array<String>(1) {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION)
            }

        } else {
            setupCameraPreviewView()
        }

        gestureDetector = GestureDetector(this, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupCameraPreviewView()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        filterId = item.itemId

        if (filterId == R.id.capture) {
            val captureResult = if(capture()) {
                "The capture has been saved to your sdcard root path."
            } else {
                "Save failed!"    
            }
            Toast.makeText(this, captureResult, Toast.LENGTH_SHORT).show()
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
    private fun setupCameraPreviewView() {
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

    private fun capture(): Boolean {
        val path = genSaveFileName(title.toString() + "_")
        val imageFile = File(path)
        if (imageFile.exists()) {
            imageFile.delete()
        }

        // create bitmap screen capture
        val bitmap = textureView.bitmap

        try {
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                outputStream.flush()
            }
        } catch (ex: FileNotFoundException) {
            Timber.e(ex)
            return false
        } catch (ex: IOException) {
            Timber.e(ex)
            return false
        }

        return true
    }

    @Suppress("DEPRECATION")
    private fun genSaveFileName(prefix: String): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.US)
        val timeString = dateFormat.format(date)
        val externalPath = Environment.getExternalStorageDirectory().toString()
        return "$externalPath/$prefix$timeString.png"
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