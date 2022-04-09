package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

/**
 * Shared list of states
 */
enum class State {
    /**
     * The start state for every SM
     */
    INITIAL,

    /**
     * The main state
     */
    MAIN,

    /**
     * GL filters settings are visible
     */
    SETTINGS_VISIBLE,

    /**
     * The save dialog is on screen
     */
    SAVE_DIALOG_IS_SHOWN,

    /**
     * A final state - we must move to "No filters" SM
     */
    NO_FILTERS,

    /**
     * A final state - we must move to "GL filters" SM
     */
    GL_FILTERS,

    /**
     * A final state - we must move to "Canceling" SM
     */
    CANCELING,

    /**
     * A final state - we must move to the previous SM
     */
    PREVIOUS_MODE,

    /**
     * A final state - the game is over, we must close the editor
     */
    FINAL
}