package com.shevelev.wizard_camera.shared.binding_adapters

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData

enum class ButtonState {
    ENABLED,
    SELECTED,
    DISABLED
}

@BindingAdapter("button_state")
fun setButtonState(view: ImageButton, value: ButtonState?) =
    value?.let {
        when(it) {
            ButtonState.DISABLED -> {
                view.isSelected = false
                view.isEnabled = false
            }
            ButtonState.SELECTED -> {
                view.isSelected = true
                view.isEnabled = true
            }
            ButtonState.ENABLED -> {
                view.isSelected = false
                view.isEnabled = true
            }
        }
    }