package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import android.graphics.Bitmap
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FiltersListData

/**
 * Base class for all output commands
 */
sealed class OutputCommand

// region Setting image to the editor
data class SetInitialImage(val image: Bitmap, val settings: GlFilterSettings) : OutputCommand()
// endregion

// region Adding and removing selection from a button
data class SelectButton(val code: ModeButtonCode) : OutputCommand()
data class UnSelectButton(val code: ModeButtonCode) : OutputCommand()
// endregion

// region Setting a filter
data class UpdateSystemFilter(val settings: SystemFilterSettings) : OutputCommand()
// endregion

// region Commands for the GL filter carousel
object ShowGlFilterCarousel : OutputCommand()
data class IntiGlFilterCarousel(val filterData: FiltersListData) : OutputCommand()
object HideGlFilterCarousel : OutputCommand()
// endregion

// region Commands for GL filter settings
data class ShowGlFilterSettings(val settings: GlFilterSettings) : OutputCommand()
object HideGlFilterSettings : OutputCommand()
// endregion

// region Commands for the system filter carousel
object ShowSystemFilterCarousel : OutputCommand()
data class ScrollSystemFilterCarousel(val filter: SystemFilterSettings) : OutputCommand()
object HideSystemFilterCarousel : OutputCommand()
// endregion

// region Crop
object ShowCroppingImage : OutputCommand()
object HideCroppingImage : OutputCommand()
// endregion

// region Saving
object ShowSaveDialog : OutputCommand()
// endregion

// region Shared
object CloseEditor : OutputCommand()
// endregion
