package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl.GlFiltersMachine
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

/**
 * Base class for all user's actions
 */
sealed class InputEvent

/**
 * Moves SM from the Init state to the Main state
 */
object Init : InputEvent()

/**
 * Moves [GlFiltersMachine] from the Init state to the Main state
 */
data class InitGlFiltersMachine(val group: FiltersGroup) : InputEvent()

/**
 * The editor mode has been switched by user
 */
data class ModeButtonClicked(val code: ModeButtonCode) : InputEvent()

object FiltersMenuButtonClicked : InputEvent()

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
data class GlFilterSwitched(val filterCode: GlFilterCode) : InputEvent()

/**
 * A user show current filter settings
 */
data class GlFilterSettingsShown(val code: GlFilterCode) : InputEvent()

/**
 * A user show current filter settings
 */
data class GlFilterFavoriteUpdate(val code: GlFilterCode, val isSelected: Boolean) : InputEvent()

/**
 * The current settings have been update by a user
 */
data class GlFilterSettingsUpdated(val settings: GlFilterSettings) : InputEvent()

/**
 * A user hid current filter settings
 */
object GlFilterSettingsHid : InputEvent()

data class FilterFromMenuSelected(val group: FiltersGroup) : InputEvent()