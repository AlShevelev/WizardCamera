package com.shevelev.wizard_camera.main_activity.view.gestures

sealed class Gesture {
    object FlingRight : Gesture()
    object FlingLeft : Gesture()
}