package com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.shevelev.wizard_camera.core.ui_utils.animation.AnimationUtils

private const val SHOW_ANIMATION_DURATION = 300L
private const val HIDE_ANIMATION_DURATION = 200L

class FlowerMenu
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var views: List<FlowerMenuItem>

    private lateinit var viewPoints: List<Point>

    private lateinit var items: List<FlowerMenuItemData>

    private val centralPoint: Point
        get() = Point(width / 2, height / 2)

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun init(items: List<FlowerMenuItemData>) {
        this.items = items

        views = items.mapIndexed { index, item ->
            FlowerMenuItem(context)
                .also {
                    it.init(item)
                    it.visibility = View.INVISIBLE
                    this.addView(it)

                    // Put them in at the center
                    val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    params.gravity = Gravity.CENTER
                    it.layoutParams = params

                    it.setOnClickListener { onItemClickListener?.invoke(index) }
                }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val area = Rect(0, 0, width, height)
        viewPoints = PositionsCalculator.calculatePositions(area, views.size, (0.65 * width / 2).toInt())
    }

    fun show() {
        val animators = views.mapIndexed { index, view ->
            createShowAnimator(view, viewPoints[index], centralPoint)
        }

        animators.forEach { it.start() }
    }

    fun hide() {
        val animators = views.map {view ->
            createHideAnimator(view)
        }

        animators.forEach { it.start() }
    }

    fun setOnItemClickListener(listener: ((Int) -> Unit)?) {
        onItemClickListener = listener
    }

    private fun createShowAnimator(view: View, endPoint: Point, centralPoint: Point) : Animator {
        val xAnimation = AnimationUtils.getFloatAnimator(
            to = (endPoint.x - centralPoint.x).toFloat(),
            duration = SHOW_ANIMATION_DURATION,
            updateListener = { view.translationX = it }
        )

        val yAnimation = AnimationUtils.getFloatAnimator(
            to = (endPoint.y - centralPoint.y).toFloat(),
            duration = SHOW_ANIMATION_DURATION,
            updateListener = { view.translationY = it }
        )

        val alphaAnimation = AnimationUtils.getFloatAnimator(
            from = 0f,
            to = 1f,
            duration = SHOW_ANIMATION_DURATION,
            startListener = {
                view.alpha = 0f
                view.visibility = View.VISIBLE
            },
            updateListener = { view.alpha = it }
        )

        return AnimatorSet().apply {
            playTogether(xAnimation, yAnimation, alphaAnimation)
        }
    }

    private fun createHideAnimator(view: View) : Animator =
        AnimationUtils.getFloatAnimator(
            from = 1f,
            to = 0f,
            duration = HIDE_ANIMATION_DURATION,
            updateListener = { view.alpha = it },
            completeListener = {
                view.alpha = 0f
                view.visibility = View.INVISIBLE
            }
        )
}