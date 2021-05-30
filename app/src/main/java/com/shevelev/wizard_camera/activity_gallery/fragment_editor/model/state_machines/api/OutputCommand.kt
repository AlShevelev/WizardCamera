package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings

/**
 * Base class for all output commands
 */
sealed class OutputCommand

// region Setting image to the editor
data class SetImage(val image: Any) : OutputCommand()
// endregion

// region Adding and removing selection from a button
data class SelectButton(val code: ModeButtonCode) : OutputCommand()
data class UnSelectButton(val code: ModeButtonCode) : OutputCommand()
// endregion

// region Setting a filter
data class SetGlFilter(val settings: GlFilterSettings) : OutputCommand()
data class SetSystemFilter(val settings: SystemFilterSettings) : OutputCommand()
// endregion

// region Commands for the GL filter carousel
object ShowGlFilterCarousel : OutputCommand()
data class ScrollGlFilterCarousel(val filter: GlFilterSettings) : OutputCommand()
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

// region Commands for system filter settings
data class ShowSystemFilterSettings(val settings: SystemFilterSettings) : OutputCommand()
object HideSystemFilterSettings : OutputCommand()
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
