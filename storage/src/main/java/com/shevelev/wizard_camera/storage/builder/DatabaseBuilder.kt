package com.shevelev.wizard_camera.storage.builder

import android.content.Context
import androidx.room.Room
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.core.DbCoreImpl

object DatabaseBuilder {
    fun build(appContext: Context): DbCore =
        Room
        .databaseBuilder(appContext, DbCoreImpl::class.java, "wizard_camera.db")
        .build()
}