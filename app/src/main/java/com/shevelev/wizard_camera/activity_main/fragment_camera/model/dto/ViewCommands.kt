package com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto

import androidx.annotation.StringRes
import androidx.camera.core.impl.ImageOutputConfig
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.filters.filters_carousel.FilterListItem
import java.io.OutputStream

class ShowCapturingSuccessCommand(val screenOrientation: ScreenOrientation): ViewCommand

/**
 * @param targetStream file for a captured image
 */
class StartCaptureCommand(
    val targetStream: OutputStream,
    val isFlashLightActive: Boolean,
    @ImageOutputConfig.RotationValue
    val rotation: Int
) : ViewCommand

class ZoomCommand(val scaleFactor: Float): ViewCommand

class ResetExposureCommand: ViewCommand
class SetExposureCommand(val exposureValue: Float): ViewCommand

class NavigateToGalleryCommand: ViewCommand

class ExitCommand(@StringRes val messageResId: Int): ViewCommand

class ShowFilterSettingsCommand(val settings: GlFilterSettings): ViewCommand
class HideFilterSettingsCommand : ViewCommand

class ScrollToFilter(val filterId: GlFilterCode): ViewCommand
class ScrollToFavoriteFilter(val filterId: GlFilterCode): ViewCommand