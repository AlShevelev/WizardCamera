package com.shevelev.wizard_camera.shared.fragment_data_pass

/**
 * Base class for passing data across fragments via push model (request - response)
 */
abstract class FragmentsDataPushBase {
    private val dataStorage: MutableMap<Int, Any> by lazy { mutableMapOf() }

    protected fun put(key: Int, value: Any) {
        dataStorage[key] = value
    }

    /**
     * Gets data item from the storage
     */
    protected fun get(key: Int): Any? = dataStorage[key]

    /**
     * Gets data item from the storage and remove the item
     */
    protected fun extract(key: Int): Any? = dataStorage.remove(key)

    protected fun contains(key: Int): Boolean = dataStorage.contains(key)
}