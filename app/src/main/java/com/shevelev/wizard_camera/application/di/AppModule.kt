package com.shevelev.wizard_camera.application.di

import android.app.Application
import android.content.Context
import com.shevelev.wizard_camera.application.di.scopes.ApplicationScope
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.utils.logging.TimberTreeDebug
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
    internal fun provideTimberTree(): Timber.Tree = TimberTreeDebug()
}