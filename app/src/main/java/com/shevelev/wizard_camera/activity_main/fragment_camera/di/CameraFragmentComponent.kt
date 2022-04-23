package com.shevelev.wizard_camera.activity_main.fragment_camera.di

import com.shevelev.wizard_camera.activity_main.fragment_camera.view.CameraFragment
import com.shevelev.wizard_camera.core.common_entities.di_scopes.FragmentScope
import dagger.Subcomponent

@Subcomponent(modules = [CameraFragmentDaggerModule::class])
@FragmentScope
interface CameraFragmentComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): CameraFragmentComponent
    }

    fun inject(fragment: CameraFragment)
}