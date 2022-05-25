package com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.shevelev.wizard_camera.core.ui_kit.lib.R

internal class FlowerMenuItem
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val itemButton by lazy { findViewById<ImageButton>(R.id.itemButton) }
    private val itemText by lazy { findViewById<TextView>(R.id.itemText) }

    private var onItemClickListener: (() -> Unit)? = null

    init {
        inflate(context, R.layout.view_flower_menu_item, this)
    }

    fun init(item: FlowerMenuItemData) {
        itemButton.setImageResource(item.iconResId)
        itemText.text = item.text

        itemButton.setOnClickListener { onItemClickListener?.invoke() }
        itemText.setOnClickListener { onItemClickListener?.invoke() }
    }

    fun setOnClickListener(listener: (() -> Unit)?) {
        onItemClickListener = listener
    }
}