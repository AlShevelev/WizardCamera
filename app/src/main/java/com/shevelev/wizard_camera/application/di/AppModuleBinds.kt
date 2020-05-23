package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.files.FilesHelperImpl
import dagger.Binds
import dagger.Module

@Module
abstract class AppModuleBinds {
    @Binds
    abstract fun provideFilesHelper(helper: FilesHelperImpl): FilesHelper
}