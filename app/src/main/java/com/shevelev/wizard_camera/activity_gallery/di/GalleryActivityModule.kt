package com.shevelev.wizard_camera.activity_gallery.di

import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPassImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

private const val SCOPE_ID = "GALLERY_ACTIVITY_DEPOSITS_SCOPE_ID"
private const val SCOPE = "GALLERY_ACTIVITY_SCOPE"

object GalleryActivityScope {
    fun getScope() = getKoin().getOrCreateScope(SCOPE_ID, named(SCOPE))
    fun closeScope() = getKoin().getScopeOrNull(SCOPE_ID)?.close()
}

val GalleryActivityModule = module(createdAtStart = false) {
    scope(named(SCOPE)) {
        scoped<FragmentsDataPass> {
            FragmentsDataPassImpl()
        }
    }
}
