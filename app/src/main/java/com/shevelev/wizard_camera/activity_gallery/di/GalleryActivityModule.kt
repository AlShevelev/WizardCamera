package com.shevelev.wizard_camera.activity_gallery.di

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPassImpl
import com.shevelev.wizard_camera.core.common_entities.di_scopes.ActivityScope
import dagger.Binds
import dagger.Module

@Module(subcomponents = [
    GalleryPageFragmentComponent::class,
    GalleryFragmentComponent::class,
    EditorFragmentComponent::class
])
abstract class GalleryActivityModule {
    @Binds
    @ActivityScope
    abstract fun provideFragmentsDataPass(dataPass: FragmentsDataPassImpl): FragmentsDataPass
}