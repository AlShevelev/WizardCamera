package com.shevelev.wizard_camera.activity_gallery.di

import com.shevelev.wizard_camera.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.activity_gallery.GalleryActivity
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import dagger.Subcomponent

@Subcomponent(modules = [GalleryActivityModuleChilds::class])
@ActivityScope
interface GalleryActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): GalleryActivityComponent
    }

    val galleryFragment: GalleryFragmentComponent.Builder
    val galleryPageFragment: GalleryPageFragmentComponent.Builder

    fun inject(activity: GalleryActivity)
}