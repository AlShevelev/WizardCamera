package com.shevelev.wizard_camera.core.utils.useful_ext

import java.text.DecimalFormat

fun Float.format(template: String): String =
    DecimalFormat(template)
        .apply {
            val symbols = decimalFormatSymbols
            symbols.decimalSeparator = '.'
            decimalFormatSymbols = symbols
        }
        .format(this.toDouble())
