package com.shevelev.wizard_camera.core.ui_utils.fragment_data_pass

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class FragmentEventPassBase<E> {
    private val eventFlow = MutableSharedFlow<E>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    val event: SharedFlow<E> = eventFlow

    fun emitEvent(event: E) {
        eventFlow.tryEmit(event)
    }
}