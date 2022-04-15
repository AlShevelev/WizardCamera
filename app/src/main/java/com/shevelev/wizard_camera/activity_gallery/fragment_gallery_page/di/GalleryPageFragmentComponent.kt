package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di

import com.shevelev.wizard_camera.core.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.GalleryPageFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface GalleryPageFragmentComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): GalleryPageFragmentComponent
    }

    fun inject(fragment: GalleryPageFragment)
}