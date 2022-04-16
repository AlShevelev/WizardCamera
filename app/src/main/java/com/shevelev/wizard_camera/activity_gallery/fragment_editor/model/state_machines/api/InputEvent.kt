package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.display_data.FilterDisplayId

/**
 * Base class for all user's actions
 */
sealed class InputEvent

/**
 * Moves SM from the Init state to the Main state
 */
object Init : InputEvent()

/**
 * The editor mode has been switched by user
 */
data class ModeButtonClicked(val code: ModeButtonCode) : InputEvent()

/**
 * Accept action
 */
object AcceptClicked : InputEvent()

/**
 * Cancel action
 */
object CancelClicked : InputEvent()

/**
 * A new filter has been selected in the carousel
 */
data class GlFilterSwitched(val filterId: FilterDisplayId) : InputEvent()

/**
 * A user show current filter settings
 */
object GlFilterSettingsShown : InputEvent()

/**
 * The current settings have been update by a user
 */
data class GlFilterSettingsUpdated(val settings: GlFilterSettings) : InputEvent()

/**
 * A user hided current filter settings
 */
object GlFilterSettingsHided : InputEvent()