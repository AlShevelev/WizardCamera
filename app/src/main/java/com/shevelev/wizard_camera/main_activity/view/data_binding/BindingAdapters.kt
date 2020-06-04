package com.shevelev.wizard_camera.main_activity.view.data_binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.shevelev.wizard_camera.main_activity.dto.ButtonState
import com.shevelev.wizard_camera.main_activity.dto.FiltersModeButtonState
import com.shevelev.wizard_camera.main_activity.view.widgets.FiltersModeButton
import com.shevelev.wizard_camera.main_activity.view.widgets.TitleWidget

@BindingAdapter("button_state")
fun setButtonState(view: ImageView, valueToBind: LiveData<ButtonState>?) =
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
            ButtonState.ACTIVE -> {
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