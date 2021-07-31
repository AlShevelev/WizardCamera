package com.shevelev.wizard_camera.activity_gallery.di

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPassImpl
import com.shevelev.wizard_camera.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.shared.bitmap.BitmapHelper
import com.shevelev.wizard_camera.shared.bitmap.BitmapHelperImpl
import dagger.Binds
import dagger.Module

@Module(subcomponents = [
    GalleryPageFragmentComponent::class,
    GalleryFragmentComponent::class,
    EditorFragmentComponent::class
])
abstract class GalleryActivityModule {
    @Binds
    abstract fun provideBitmapHelper(helper: BitmapHelperImpl): BitmapHelper

    @Binds
    @ActivityScope
    abstract fun provideFragmentsDataPass(dataPass: FragmentsDataPassImpl): FragmentsDataPass
}