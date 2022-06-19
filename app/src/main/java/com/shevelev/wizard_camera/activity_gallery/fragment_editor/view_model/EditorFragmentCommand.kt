package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

internal sealed class EditorFragmentCommand {
    object ShowEditorSaveDialog : EditorFragmentCommand()

    object CloseEditor : EditorFragmentCommand()

    data class SetFlowerMenuVisibility(val isVisible: Boolean) : EditorFragmentCommand()
}