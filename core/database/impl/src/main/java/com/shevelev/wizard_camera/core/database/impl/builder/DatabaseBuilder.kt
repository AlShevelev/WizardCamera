package com.shevelev.wizard_camera.core.database.impl.builder

import android.content.Context
import androidx.room.Room
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.core.DbCoreImpl

object DatabaseBuilder {
    fun build(appContext: Context): DbCore =
        Room
        .databaseBuilder(appContext, DbCoreImpl::class.java, "wizard_camera.db")
        .build()
}