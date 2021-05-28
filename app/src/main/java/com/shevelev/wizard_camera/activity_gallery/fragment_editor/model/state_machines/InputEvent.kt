package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines

import android.graphics.Rect
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings

/**
 * Base class for all user's actions
 */
sealed class InputEvent

/**
 * Moves SM from the Init state to the Main state
 */
object Init : InputEvent()

// Click to the buttons
data class ModeButtonClicked(val code: ModeButtonCode) : InputEvent()

// Actions
object AcceptClicked : InputEvent()
object CancelClicked : InputEvent()

// GL Filters
data class GlFilterSwitched(val settings: GlFilterSettings) : InputEvent()
object GlFilterSettingsStarted : InputEvent()
data class GlFilterSettingsUpdated(val settings: GlFilterSettings) : InputEvent()
object GlFilterSettingsEnded : InputEvent()

// System filters
data class SystemFilterSwitched(val settings: SystemFilterSettings) : InputEvent()
data class SystemFilterSettingsUpdated(val settings: SystemFilterSettings) : InputEvent()

// Cutting
data class CuttingGridUpdated(val gridRect: Rect) : InputEvent()