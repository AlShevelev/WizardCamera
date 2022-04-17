package com.shevelev.wizard_camera.application.di

import android.app.Application
import android.content.Context
import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.application.build_info.BuildInfoImpl
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.common_entities.di_scopes.ApplicationScope
import com.shevelev.wizard_camera.core.database.impl.builder.DatabaseBuilder
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.camera_gl.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.core.crashlytics.api.CrashlyticsFacade
import com.shevelev.wizard_camera.core.utils.logging.TimberTreeDebug
import com.shevelev.wizard_camera.core.utils.logging.TimberTreeRelease
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

@Module
class AppModule(
    private val app: Application
) {
    @Provides
    @ApplicationScope
    internal fun provideContext(): Context = app.applicationContext

    @Provides
    internal fun provideDispatchersProvider(): DispatchersProvider = object : DispatchersProvider {
        override val uiDispatcher: CoroutineDispatcher
            get() = Dispatchers.Main
        override val calculationsDispatcher: CoroutineDispatcher
            get() = Dispatchers.Default
        override val ioDispatcher: CoroutineDispatcher
            get() = Dispatchers.IO
    }

    @Provides
    internal fun provideTimberTree(crashlytics: CrashlyticsFacade): Timber.Tree =
        when(BuildConfig.FLAVOR) {
            "dev" -> TimberTreeDebug()
            "prod" -> TimberTreeRelease(crashlytics)
            else -> throw UnsupportedOperationException("This flavor is not supported: ${BuildConfig.FLAVOR}")
        }

    @Provides
    @ApplicationScope
    internal fun provideRoomDbCore(appContext: Context): DbCore = DatabaseBuilder.build(appContext)

    @Provides
    internal fun provideMoshi(): Moshi =
        Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}