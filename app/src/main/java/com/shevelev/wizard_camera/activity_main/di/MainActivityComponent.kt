package com.shevelev.wizard_camera.activity_main.di

import com.shevelev.wizard_camera.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.activity_main.MainActivity
import com.shevelev.wizard_camera.activity_main.fragment_camera.di.CameraFragmentComponent
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModuleChild::class])
@ActivityScope
interface MainActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainActivityComponent
    }

    val cameraFragment: CameraFragmentComponent.Builder

    fun inject(activity: MainActivity)
}