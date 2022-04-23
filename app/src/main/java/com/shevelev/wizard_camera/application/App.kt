package com.shevelev.wizard_camera.application

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityModule
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentModule
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentModule
import com.shevelev.wizard_camera.activity_main.fragment_camera.di.CameraFragmentModule
import com.shevelev.wizard_camera.application.di.AppModule
import com.shevelev.wizard_camera.application.di.BuildInfoModule
import com.shevelev.wizard_camera.core.bitmaps.impl.di.BitmapsModule
import com.shevelev.wizard_camera.core.camera_gl.impl.di.CameraGlModule
import com.shevelev.wizard_camera.core.catalano.impl.di.CatalanoModule
import com.shevelev.wizard_camera.core.crashlytics.impl.di.CrashlyticsModule
import com.shevelev.wizard_camera.core.database.impl.di.DatabaseModule
import com.shevelev.wizard_camera.core.photo_files.impl.di.PhotoFilesModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {
    private val timberTree: Timber.Tree by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                // App
                AppModule,

                // Core
                BitmapsModule,
                BuildInfoModule,
                CameraGlModule,
                CatalanoModule,
                CrashlyticsModule,
                DatabaseModule,
                PhotoFilesModule,

                // Main activity
                CameraFragmentModule,

                // Gallery activity
                GalleryActivityModule,
                EditorFragmentModule,
                GalleryFragmentModule
            )
        }

        AndroidThreeTen.init(this)

        Timber.plant(timberTree)
    }
}