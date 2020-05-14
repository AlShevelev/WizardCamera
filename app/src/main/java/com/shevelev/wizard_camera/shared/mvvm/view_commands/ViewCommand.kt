package com.shevelev.wizard_camera.shared.mvvm.view_commands

import androidx.annotation.StringRes

interface ViewCommand

class ShowMessageResCommand(@StringRes val textResId: Int, val isError: Boolean = true): ViewCommand
class ShowMessageTextCommand(val text: String, val isError: Boolean = true): ViewCommand
