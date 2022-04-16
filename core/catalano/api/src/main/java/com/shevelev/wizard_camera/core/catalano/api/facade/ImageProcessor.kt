package com.shevelev.wizard_camera.core.catalano.api.facade

import android.graphics.Bitmap

/**
 * It is an interface for processing images using the Catalano library
 */
interface ImageProcessor {
    /**
     * Process the giving bitmap using Histogram Equalization algorithm
     */
    fun processHistogramEqualization(source: Bitmap): Bitmap
}