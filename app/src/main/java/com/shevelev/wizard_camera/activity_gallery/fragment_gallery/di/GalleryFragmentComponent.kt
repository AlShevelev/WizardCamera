package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di

import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.GalleryFragment
import com.shevelev.wizard_camera.core.common_entities.di_scopes.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [GalleryFragmentDaggerModule::class])
@FragmentScope
interface GalleryFragmentComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): GalleryFragmentComponent
    }

    fun inject(fragment: GalleryFragment)
}