package com.shevelev.wizard_camera.core.catalano.impl.facade

import android.graphics.Bitmap
import com.shevelev.wizard_camera.core.catalano.api.facade.ImageProcessor
import com.shevelev.wizard_camera.core.catalano.impl.fast_bitmap.FastBitmap
import com.shevelev.wizard_camera.core.catalano.impl.filters.HistogramEqualization

/**
 * It's an images processor using the Catalano library
 */
class ImageProcessorImpl : ImageProcessor {
    /**
     * Process the giving bitmap using Histogram Equalization algorithm
     */
    override fun processHistogramEqualization(source: Bitmap): Bitmap {
        val sourceCopy = source.copy(source.config, true)

        val fastBitmap = FastBitmap(sourceCopy)

        val histogramEqualization = HistogramEqualization()
        histogramEqualization.applyInPlace(fastBitmap)

        return fastBitmap.toBitmap()
    }
}