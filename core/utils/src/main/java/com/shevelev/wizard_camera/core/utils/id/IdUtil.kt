package com.shevelev.wizard_camera.core.utils.id

import java.nio.ByteBuffer
import java.util.*

object IdUtil {

    /** */
    fun generateStringId(): String {
        val toString = UUID.randomUUID().toString()
        return toString
    }

    /** */
    fun generateLongId(): Long {
        val id = UUID.randomUUID()

        val buffer = ByteBuffer.wrap(ByteArray(Long.SIZE_BYTES*2))
        buffer.putLong(id.leastSignificantBits)
        buffer.putLong(id.mostSignificantBits)
        return MurmurHash.hash64(buffer.array())
    }

    /** */
    fun generateIntId(): Int {
        val longBuffer = ByteBuffer.allocate(Long.SIZE_BYTES)
        longBuffer.putLong(generateLongId())

        val intBuffer = ByteBuffer.allocate(Int.SIZE_BYTES)
        for(i in 0 until Int.SIZE_BYTES) {
            intBuffer.put(longBuffer[i*2])
        }

        return intBuffer.getInt(0)
    }
}

/** Convert string to Id */
fun String.toLongId(): Long = MurmurHash.hash64(this)