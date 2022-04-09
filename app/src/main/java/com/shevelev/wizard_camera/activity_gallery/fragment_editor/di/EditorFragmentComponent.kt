package com.shevelev.wizard_camera.activity_gallery.fragment_editor.di

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view.EditorFragment
import com.shevelev.wizard_camera.common_entities.di_scopes.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [EditorFragmentModule::class])
@FragmentScope
interface EditorFragmentComponent {
    @Subcomponent.Builder
    interface Builder {
        fun init(module: EditorFragmentModule): Builder
        fun build(): EditorFragmentComponent
    }

    fun inject(fragment: EditorFragment)
}