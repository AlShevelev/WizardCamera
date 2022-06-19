package com.shevelev.wizard_camera.activity_main.fragment_camera.view.data_binding

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.TitleWidget

@BindingAdapter("title_text")
fun setTitleText(view: TitleWidget, value: String?) =
    value?.let {
        view.show(it)
    }

@BindingAdapter("button_enabled")
fun setButtonEnabled(view: ImageButton, value: Boolean?) =
    value?.let {
        view.isEnabled = it
    }
