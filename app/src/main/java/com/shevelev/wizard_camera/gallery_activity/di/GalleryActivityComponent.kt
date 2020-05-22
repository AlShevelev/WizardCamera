package com.shevelev.wizard_camera.gallery_activity.di

import com.shevelev.wizard_camera.application.di.scopes.ActivityScope
import com.shevelev.wizard_camera.gallery_activity.view.GalleryActivity
import dagger.Subcomponent

@Subcomponent(modules = [GalleryActivityModuleBinds::class])
@ActivityScope
interface GalleryActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): GalleryActivityComponent
    }

    fun inject(activity: GalleryActivity)
}