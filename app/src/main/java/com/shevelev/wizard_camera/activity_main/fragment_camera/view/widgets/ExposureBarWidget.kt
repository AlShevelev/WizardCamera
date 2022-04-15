package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.util.Range
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.utils.useful_ext.fitInRange
import com.shevelev.wizard_camera.core.utils.useful_ext.reduceToRange

class ExposureBarWidget
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val drawingRect = RectF(0f, 0f, 0f, 0f)
    private val drawingPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val strokeWidth: Float

    @ColorInt
    private val strokeColor: Int
    @ColorInt
    private val buttonFillColor: Int
    @ColorInt
    private val buttonFillColorPressed: Int

    private val icon: VectorDrawable

    private val valuesRange: Range<Float>

    private var centerX = 0f
    private var minY = 0f
    private var maxY = 0f

    private var buttonCenterY = 0f
    private var buttonRadiusExt = 0f
    private var buttonRadiusInt = 0f
    private var buttonMinY = 0f
    private var buttonMaxY = 0f
    private var buttonCenterBoundsRange = Range(0f, 0f)

    private var iconTranslationX = 0f
    private var iconTranslationY = 0f

    private var isInDragMode = false

    private var onValueChangeListener: ((Float) -> Unit)? = null

    private var oldOutputValue = Float.MIN_VALUE

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExposureBarWidget)

        icon =  typedArray.getDrawable(R.styleable.ExposureBarWidget_exposure_button_icon) as VectorDrawable
        val iconSize = typedArray.getDimensionPixelSize(R.styleable.ExposureBarWidget_exposure_button_icon_size, 0)
        icon.setBounds(0, 0, iconSize, iconSize)

        strokeWidth = typedArray.getDimensionPixelSize(R.styleable.ExposureBarWidget_exposure_stroke_width, 0).toFloat()
        drawingPaint.strokeWidth = strokeWidth

        strokeColor = typedArray.getColor(R.styleable.ExposureBarWidget_exposure_stroke_color, Color.WHITE)
        buttonFillColor = typedArray.getColor(R.styleable.ExposureBarWidget_exposure_button_color, Color.BLACK)
        buttonFillColorPressed = typedArray.getColor(R.styleable.ExposureBarWidget_exposure_button_color_pressed, Color.BLUE)

        val minValue = typedArray.getFloat(R.styleable.ExposureBarWidget_exposure_min_value, 0f)
        val maxValue = typedArray.getFloat(R.styleable.ExposureBarWidget_exposure_max_value, 0f)
        valuesRange = Range(minValue, maxValue)

        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawingRect.right = width.toFloat()
        drawingRect.bottom = height.toFloat()

        buttonCenterY = drawingRect.centerY()
        recalculateDrawingValues()
    }

    override fun onDraw(canvas: Canvas) {
        drawingPaint.color = strokeColor
        drawingPaint.style = Paint.Style.STROKE

        // Draw the center line
        canvas.drawLine(centerX, minY, centerX, buttonMinY, drawingPaint)
        canvas.drawLine(centerX, buttonMaxY, centerX, maxY, drawingPaint)

        // Draw the button
        canvas.drawCircle(centerX, buttonCenterY, buttonRadiusExt, drawingPaint)
        drawingPaint.style = Paint.Style.FILL
        drawingPaint.color = if(isInDragMode) buttonFillColorPressed else buttonFillColor
        canvas.drawCircle(centerX, buttonCenterY, buttonRadiusInt, drawingPaint)

        // Draw button icon
        canvas.translate(iconTranslationX, iconTranslationY)
        icon.draw(canvas)
        canvas.translate(-iconTranslationX, -iconTranslationY)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(!isEnabled) {
            return false
        }

        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if(isButtonHit(event.y)){
                    isInDragMode = true
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(isInDragMode) {
                    buttonCenterY = event.y.fitInRange(buttonCenterBoundsRange)
                    recalculateDrawingValues()
                    invalidate()

                    val outputValue = buttonCenterY.reduceToRange(buttonCenterBoundsRange, valuesRange)
                    if (outputValue != oldOutputValue) {
                        onValueChangeListener?.invoke(outputValue)
                        oldOutputValue = outputValue
                    }
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isInDragMode = false
                invalidate()
            }
        }
        return true
    }

    fun setOnValueChangeListener(listener: ((Float) -> Unit)?) {
        onValueChangeListener = listener
    }

    fun reset() {
        buttonCenterY = drawingRect.centerY()
        recalculateDrawingValues()
        invalidate()
    }
    
    private fun recalculateDrawingValues() {
        centerX = drawingRect.left + drawingRect.width()/2
        minY = drawingRect.top
        maxY = drawingRect.bottom

        buttonRadiusExt = drawingRect.width() / 2 - strokeWidth / 2
        buttonRadiusInt = buttonRadiusExt - strokeWidth / 2
        buttonMinY = buttonCenterY - buttonRadiusExt
        buttonMaxY = buttonCenterY + buttonRadiusExt

        val buttonCenterMinY = minY + buttonRadiusExt + strokeWidth / 2
        val buttonCenterMaxY = maxY - buttonRadiusExt - strokeWidth / 2
        buttonCenterBoundsRange = Range(buttonCenterMinY, buttonCenterMaxY)

        iconTranslationX = centerX - icon.bounds.width() / 2
        iconTranslationY = buttonCenterY - icon.bounds.height() / 2
    }

    private fun isButtonHit(y: Float): Boolean {
        return y >= buttonMinY && y < buttonMaxY
    }
}