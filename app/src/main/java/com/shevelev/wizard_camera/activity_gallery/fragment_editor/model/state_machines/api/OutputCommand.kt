package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import android.graphics.Bitmap
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FiltersListData

/**
 * Base class for all output commands
 */
sealed class OutputCommand

/**
 * Sets initial image and its filter
 */
data class SetInitialImage(val image: Bitmap, val settings: GlFilterSettings) : OutputCommand()

/**
 * Moves a button to selected state
 */
data class SelectButton(val code: ModeButtonCode) : OutputCommand()

/**
 * Moves a button to unselected state
 */
data class UnSelectButton(val code: ModeButtonCode) : OutputCommand()

/**
 * Updates system filter settings
 */
data class UpdateSystemFilter(val settings: SystemFilterSettings) : OutputCommand()

/**
 * Shows the carousel for GL filters
 */
object ShowGlFilterCarousel : OutputCommand()

/**
 * Sets initial value to the carousel of GL filters
 */
data class IntiGlFilterCarousel(val filterData: FiltersListData) : OutputCommand()

/**
 * Sets new GL filter to an image
 */
data class UpdateImageByGlFilter(val settings: GlFilterSettings) : OutputCommand()

/**
 * Hides the carousel for GL filters
 */
object HideGlFilterCarousel : OutputCommand()

/**
 * Shows settings for GL filters
 */
data class ShowGlFilterSettings(val settings: GlFilterSettings) : OutputCommand()

/**
 * Hides settings for GL filters
 */
object HideGlFilterSettings : OutputCommand()

/**
 * Shows the carousel for system filters
 */
object ShowSystemFilterCarousel : OutputCommand()

/**
 * Scrolls the carousel for system filters to some filter
 */
data class ScrollSystemFilterCarousel(val filter: SystemFilterSettings) : OutputCommand()

/**
 * Hides the carousel for system filters
 */
object HideSystemFilterCarousel : OutputCommand()

/**
 * Shows cropping image
 */
object ShowCroppingImage : OutputCommand()

/**
 * Hides cropping image
 */
object HideCroppingImage : OutputCommand()
// endregion

/**
 * Shows saving dialog
 */
object ShowSaveDialog : OutputCommand()

/**
 * Closes the editor
 */
object CloseEditor : OutputCommand()