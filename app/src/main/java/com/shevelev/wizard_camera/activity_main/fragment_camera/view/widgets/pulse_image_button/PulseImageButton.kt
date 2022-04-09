package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.pulse_image_button

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.widget.ImageView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.shared.animation.AnimationUtils

@SuppressLint("AppCompatCustomView")
class PulseImageButton
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr)  {

    private var animSet: AnimatorSet? = null

    private val activeIcon: VectorDrawable
    private val inactiveIcon: VectorDrawable

    private val minScale = 0.8f
    private val maxScale = 1f

    var isActive = false
    set(value) {
        field = value
        setImageDrawable(if(field) activeIcon else inactiveIcon)
    }

    private var onClickListener: ((Boolean) -> Unit)? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseImageButton)

        activeIcon =  typedArray.getDrawable(R.styleable.PulseImageButton_pulse_button_active_icon) as VectorDrawable
        inactiveIcon =  typedArray.getDrawable(R.styleable.PulseImageButton_pulse_button_inactive_icon) as VectorDrawable

        typedArray.recycle()

        scaleType = ScaleType.FIT_XY
        scaleX = maxScale
        scaleY = maxScale

        setImageDrawable(inactiveIcon)

        super.setOnClickListener { processClick() }
    }

    fun setOnPulseButtonClickListener(listener: ((Boolean) -> Unit)?) {
        onClickListener = listener
    }

    private fun processClick() {
        animSet?.cancel()

        scaleX = maxScale
        scaleY = maxScale

        val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        val reduceAnimation = AnimationUtils.getFloatAnimator(
            duration = duration,
            from = maxScale,
            to = minScale,
            updateListener = {
                scaleX = it
                scaleY = it
            },
            completeListener = {
                isActive = !isActive
            }
        )
        val increaseAnimation = AnimationUtils.getFloatAnimator(
            duration = duration,
            from = minScale,
            to = maxScale,
            updateListener = {
                scaleX = it
                scaleY = it
            },
            completeListener = {
                onClickListener?.invoke(isActive)
            }
        )

        animSet = AnimatorSet()
            .apply {
                playSequentially(reduceAnimation, increaseAnimation)
                start()
            }
    }
}
