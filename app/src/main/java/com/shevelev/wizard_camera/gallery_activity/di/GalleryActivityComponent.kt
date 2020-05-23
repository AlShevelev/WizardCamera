package com.shevelev.wizard_camera.gallery_activity.di

import com.shevelev.wizard_camera.application.di.scopes.ActivityScope
import com.shevelev.wizard_camera.gallery_activity.view.GalleryActivity
import com.shevelev.wizard_camera.gallery_page.di.GalleryPageFragmentComponent
import dagger.Subcomponent

@Subcomponent(modules = [GalleryActivityModuleBinds::class, GalleryActivityModuleChilds::class])
@ActivityScope
interface GalleryActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): GalleryActivityComponent
    }

    val galleryPageFragment: GalleryPageFragmentComponent.Builder

    fun inject(activity: GalleryActivity)
}