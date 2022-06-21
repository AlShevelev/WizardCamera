package com.shevelev.wizard_camera.activity_gallery.shared.event_pass

import kotlinx.coroutines.flow.SharedFlow

internal interface GalleryFragmentsEventPass {
    val event: SharedFlow<GalleryFragmentsEvent>

    fun emitEvent(event: GalleryFragmentsEvent)
}