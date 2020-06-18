package com.shevelev.wizard_camera.main_activity.di

import com.shevelev.wizard_camera.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.main_activity.view.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModuleBinds::class])
@ActivityScope
interface MainActivityComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}