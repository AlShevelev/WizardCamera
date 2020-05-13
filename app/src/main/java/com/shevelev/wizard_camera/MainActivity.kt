package com.shevelev.wizard_camera

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.shevelev.wizard_camera.camera.camera_renderer.CameraRenderer
import com.shevelev.wizard_camera.camera.filter.FilterCode
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
import kotlin.math.abs
import kotlin.system.exitProcess

@RuntimePermissions
class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private var renderer: CameraRenderer? = null
    private var textureView: TextureView? = null

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

        setContentView(R.layout.activity_main)

        setTitle(titles[currentFilterId])

        gestureDetector = GestureDetector(this, this)

        shootButton.setOnClickListener { capture() }
    }

    override fun onResume() {
        super.onResume()
        setupCameraPreviewViewWithPermissionCheck()
    }

    override fun onPause() {
        super.onPause()
        releaseCameraPreviewView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.filter, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        filterId = item.itemId
//
//        if (filterId == R.id.capture) {
//            capture()
//            return true
//        }
//
//        title = item.title
//
//        renderer!!.setSelectedFilter(getFilterCode(filterId))
//
//        currentFilterId = filterResIds.indexOf(filterId)
//        return true
//    }

    override fun onShowPress(e: MotionEvent?) {
        // do nothing
    }

    override fun onSingleTapUp(e: MotionEvent?) = false

    override fun onDown(e: MotionEvent?) = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if(abs(velocityX) < abs(velocityY)) {
            return true
        }

        val step = if(velocityX > 0) -1 else 1
        
        currentFilterId = circleLoop(titles.size, currentFilterId, step)
        setTitle(titles[currentFilterId])
        
        renderer!!.setSelectedFilter(getFilterCode(filterResIds[currentFilterId]))
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

    override fun onLongPress(e: MotionEvent?) {
        // do nothing
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @NeedsPermission(Manifest.permission.CAMERA)
    internal fun setupCameraPreviewView() {
        renderer = CameraRenderer(this)
        textureView = TextureView(this)
        root.addView(textureView)
        textureView!!.surfaceTextureListener = renderer

        textureView!!.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun releaseCameraPreviewView() {
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

        val dir = File(externalPath, getString(R.string.app_name))
        if(!dir.exists()) {
            dir.mkdir()
        }

        return File(dir, "$prefix$timeString.webp")
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

    private fun getFilterCode(@IdRes id: Int): FilterCode =
        when(id) {
            R.id.filterOriginal -> FilterCode.ORIGINAL
            R.id.filterEdgeDectection -> FilterCode.EDGE_DETECTION
            R.id.filterPixelize -> FilterCode.PIXELIZE
            R.id.filterEMInterference -> FilterCode.EM_INTERFERENCE
            R.id.filterTrianglesMosaic -> FilterCode.TRIANGLES_MOSAIC
            R.id.filterLegofied -> FilterCode.LEGOFIED
            R.id.filterTileMosaic -> FilterCode.TILE_MOSAIC
            R.id.filterBlueorange -> FilterCode.BLUE_ORANGE
            R.id.filterChromaticAberration -> FilterCode.CHROMATIC_ABERRATION
            R.id.filterBasicDeform -> FilterCode.BASIC_DEFORM
            R.id.filterContrast -> FilterCode.CONTRAST
            R.id.filterNoiseWarp -> FilterCode.NOISE_WARP
            R.id.filterRefraction -> FilterCode.REFRACTION
            R.id.filterMapping -> FilterCode.MAPPING
            R.id.filterCrosshatch -> FilterCode.CROSSHATCH
            R.id.filterLichtensteinEsque -> FilterCode.LICHTENSTEIN_ESQUE
            R.id.filterAsciiArt -> FilterCode.ASCII_ART
            R.id.filterMoney -> FilterCode.MONEY
            R.id.filterCracked -> FilterCode.CRACKED
            R.id.filterPolygonization -> FilterCode.POLYGONIZATION
            R.id.filterBlackAndWhite -> FilterCode.BLACK_AND_WHITE
            R.id.filterGray -> FilterCode.GRAY
            R.id.filterNegative -> FilterCode.NEGATIVE
            R.id.filterNostalgia -> FilterCode.NOSTALGIA
            R.id.filterCasting -> FilterCode.CASTING
            R.id.filterRelief -> FilterCode.RELIEF
            R.id.filterSwirl -> FilterCode.SWIRL
            R.id.filterHexagonMosaic -> FilterCode.HEXAGON_MOSAIC
            R.id.filterMirror -> FilterCode.MIRROR
            R.id.filterTriple -> FilterCode.TRIPLE
            R.id.filterCartoon -> FilterCode.CARTOON
            R.id.filterWaterReflection -> FilterCode.WATER_REFLECTION
            else -> throw UnsupportedOperationException("This is not supported: $id")
        }

    private fun hideSystemUI() {
        // To make Navigation bar half-transparent
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}