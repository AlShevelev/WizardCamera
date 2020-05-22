package com.shevelev.wizard_camera.storage.core

interface DbCoreRun {
    fun <T> run(action: (DbCoreDao) -> T): T

    fun <T> runConsistent(action: (DbCoreDao) -> T): T
}