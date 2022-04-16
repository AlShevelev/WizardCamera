package com.shevelev.wizard_camera.core.camera_gl.shared.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    val uiDispatcher: CoroutineDispatcher
    val calculationsDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
}