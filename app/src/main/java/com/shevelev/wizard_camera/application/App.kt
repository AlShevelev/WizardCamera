package com.shevelev.wizard_camera.application

import android.annotation.SuppressLint
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityModule
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentModule
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentModule
import com.shevelev.wizard_camera.activity_main.fragment_camera.di.CameraFragmentModule
import com.shevelev.wizard_camera.application.di.AppComponent
import com.shevelev.wizard_camera.application.di.AppModule
import com.shevelev.wizard_camera.application.di.BuildInfoModule
import com.shevelev.wizard_camera.application.di_storage.DependencyInjectionStorage
import com.shevelev.wizard_camera.core.bitmaps.impl.di.BitmapsModule
import com.shevelev.wizard_camera.core.camera_gl.impl.di.CameraGlModule
import com.shevelev.wizard_camera.core.catalano.impl.di.CatalanoModule
import com.shevelev.wizard_camera.core.crashlytics.impl.di.CrashlyticsModule
import com.shevelev.wizard_camera.core.database.impl.di.DatabaseModule
import com.shevelev.wizard_camera.core.photo_files.impl.di.PhotoFilesModule
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber
import javax.inject.Inject
import org.koin.core.logger.Level

class App : Application() {
    @Inject
    internal lateinit var timberTree: Timber.Tree

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var injections : DependencyInjectionStorage
            private set
    }

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

        injections = DependencyInjectionStorage(this)
        injections.get<AppComponent>(IdUtil.generateStringId()).inject(this)

        Timber.plant(timberTree)
    }
}