package com.shevelev.wizard_camera.storage.builder

import android.content.Context
import androidx.room.Room
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.core.DbCoreRun

object DatabaseBuilder {
    fun build(appContext: Context): DbCoreRun =
        Room
        .databaseBuilder(appContext, DbCore::class.java, "wizard_camera.db")
        .build()
}