package com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu

import android.graphics.Point
import android.graphics.Rect
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

internal object PositionsCalculator {
    private data class PointD(
        val x: Double,
        val y: Double
    )

    /**
     * Calculates positions of all elements
     * @param distance a distance from a center of the [area] to each point
     */
    fun calculatePositions(area: Rect, itemsQuantity: Int, distance: Int): List<Point> {
        val center = PointD(area.left + area.width() / 2.0, area.top + area.height() / 2.0)

        val angleOffset = 2 * Math.PI / itemsQuantity

        val result = mutableListOf<Point>()

        for (i in 0 until itemsQuantity) {
            val currentAngle = angleOffset * i

            val x = ((distance * sin(currentAngle)) + center.x).roundToInt()
            val y = ((distance * cos(currentAngle)) + center.y).roundToInt()

            result.add(Point(x, y))
        }

        return result
    }
}