package com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout

class FlowerMenu
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var views: List<FlowerMenuItem>

    private lateinit var viewPoints: List<Point>

    fun init(items: List<FlowerMenuItemData>) {
        views = items.map { item ->
            FlowerMenuItem(context)
                .also {
                    it.init(item)
                    //it.visibility = View.INVISIBLE        // FOR DEBUG ONLY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    this.addView(it)

                    // Put them in at the center
                    val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    params.gravity = Gravity.CENTER
                    it.layoutParams = params
                }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val area = Rect(0, 0, width, height)
        viewPoints = PositionsCalculator.calculatePositions(area, views.size, (0.65 * width / 2).toInt())

        translateViews()        // FOR DEBUG ONLY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    private fun translateViews() {
        val center = Point(width / 2, height / 2)
        views.forEachIndexed { index, view ->
            val deltaX = viewPoints[index].x - center.x
            val deltaY = viewPoints[index].y - center.y

            view.translationX = deltaX.toFloat()
            view.translationY = deltaY.toFloat()
        }
    }
}