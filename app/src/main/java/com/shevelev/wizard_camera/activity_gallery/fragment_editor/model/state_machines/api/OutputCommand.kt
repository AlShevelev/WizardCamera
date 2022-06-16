package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import android.graphics.Bitmap
import androidx.annotation.StringRes
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem

/**
 * Base class for all output commands
 */
sealed class OutputCommand

/**
 * Sets initial image and its filter
 */
data class SetInitialImage(
    val image: Bitmap,
    val settings: GlFilterSettings,
    @StringRes
    val filterTitle: Int,
    val isMagicMode: Boolean
) : OutputCommand()

/**
 * Selects and removes selection from a button
 */
class SetButtonSelection(val code: ModeButtonCode, val isSelected: Boolean) : OutputCommand()

/**
 * Shows and hides the carousel for GL filters
 */
class SetGlFilterCarouselVisibility(val isVisible: Boolean) : OutputCommand()

/**
 * Sets initial value to the carousel of GL filters
 */
data class UpdateGlFilterCarousel(val items: List<FilterListItem>) : OutputCommand()

/**
 * Sets new GL filter to an image
 */
data class UpdateImageByGlFilter(
    val settings: GlFilterSettings,
    @StringRes val filterTitle: Int?,
    val filters: List<FilterListItem>
) : OutputCommand()

/**
 * Shows settings for GL filters
 */
data class ShowGlFilterSettings(val settings: GlFilterSettings) : OutputCommand()

/**
 * Hides settings for GL filters
 */
object HideGlFilterSettings : OutputCommand()

/**
 * Shows saving dialog
 */
object ShowSaveDialog : OutputCommand()

/**
 * Closes the editor
 */
object CloseEditor : OutputCommand()

/**
 * Shows and hides
 */
class SetProgressVisibility(val isVisible: Boolean) : OutputCommand()