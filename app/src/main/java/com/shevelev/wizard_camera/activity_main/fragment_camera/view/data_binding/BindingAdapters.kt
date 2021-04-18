package com.shevelev.wizard_camera.activity_main.fragment_camera.view.data_binding

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ButtonState
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersModeButtonState
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.FiltersModeButton
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.TitleWidget

@BindingAdapter("button_state")
fun setButtonState(view: ImageButton, valueToBind: LiveData<ButtonState>?) =
    valueToBind?.value?.let {
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

@BindingAdapter("button_state_filters_mode")
fun setFiltersModeButtonState(view: FiltersModeButton, valueToBind: LiveData<FiltersModeButtonState>?) =
    valueToBind?.value?.let {
        view.updateState(it)
    }

@BindingAdapter("title_text")
fun setTitleText(view: TitleWidget, valueToBind: LiveData<String>?) =
    valueToBind?.value?.let {
        view.show(it)
    }

@BindingAdapter("button_enabled")
fun setButtonEnabled(view: ImageButton, valueToBind: LiveData<Boolean>?) =
    valueToBind?.value?.let {
        view.isEnabled = it
    }