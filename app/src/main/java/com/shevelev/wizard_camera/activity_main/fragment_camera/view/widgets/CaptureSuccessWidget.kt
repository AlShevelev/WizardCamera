package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.shared.animation.AnimationUtils
import timber.log.Timber
import kotlin.random.Random

class CaptureSuccessWidget
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatImageView(context, attrs, defStyleAttr)  {

    private var animSet: AnimatorSet? = null

    private val imageIds = listOf(
        R.drawable.img_smile_1, R.drawable.img_smile_2, R.drawable.img_smile_3,
        R.drawable.img_smile_4, R.drawable.img_smile_5, R.drawable.img_smile_6)

    fun show(screenOrientation: ScreenOrientation) {
        Timber. tag("CAPTURE").d("show()")

        rotation = convertScreenOrientationToDegrees(screenOrientation)

        animSet?.cancel()

        alpha = 0f

        setImageResource(imageIds[Random.nextInt(0, imageIds.size)])

        val duration = 250L
        val showAnimation = AnimationUtils.getFloatAnimator(
            duration = duration,
            updateListener = { alpha = it }
        )
        val hideAnimation = AnimationUtils.getFloatAnimator(
            forward = false,
            duration = duration,
            updateListener = { alpha = it }
        ).apply { startDelay = duration }

        animSet = AnimatorSet()
            .apply {
                playSequentially(showAnimation, hideAnimation)
                start()
            }
    }

    private fun convertScreenOrientationToDegrees(orientation: ScreenOrientation): Float =
        when (orientation) {
            ScreenOrientation.PORTRAIT -> 0f
            ScreenOrientation.LANDSCAPE -> 90f
            ScreenOrientation.REVERSED_LANDSCAPE -> 270f
            ScreenOrientation.REVERSED_PORTRAIT -> 180f
        }
}
