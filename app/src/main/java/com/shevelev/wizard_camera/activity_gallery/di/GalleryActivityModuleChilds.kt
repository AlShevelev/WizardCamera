package com.shevelev.wizard_camera.activity_gallery.di

import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import dagger.Module

@Module(subcomponents = [
    GalleryPageFragmentComponent::class,
    GalleryFragmentComponent::class,
])
class GalleryActivityModuleChilds