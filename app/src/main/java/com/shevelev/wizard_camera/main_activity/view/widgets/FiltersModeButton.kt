package com.shevelev.wizard_camera.main_activity.view.widgets

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.main_activity.dto.FiltersMode
import com.shevelev.wizard_camera.main_activity.dto.FiltersModeButtonState
import com.shevelev.wizard_camera.shared.animation.AnimationUtils

class FiltersModeButton
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var animSet: AnimatorSet? = null

    private var filtersMode = FiltersMode.NO_FILTERS

    private var onModeChangeListener: ((FiltersMode) -> Unit)? = null

    private val noFiltersSelected by lazy { findViewById<ImageView>(R.id.noFiltersSelected) }
    private val allFiltersSelected by lazy { findViewById<ImageView>(R.id.allFiltersSelected) }
    private val favoritesFiltersSelected by lazy { findViewById<ImageView>(R.id.favoritesFiltersSelected) }
    private val noFilters by lazy { findViewById<ImageView>(R.id.noFilters) }
    private val allFilters by lazy { findViewById<ImageView>(R.id.allFilters) }
    private val favoritesFilters by lazy { findViewById<ImageView>(R.id.favoritesFilters) }

    init {
        inflate(context, R.layout.view_filters_mode, this)
        updateModeInstant(filtersMode)

        setOnClickListener { updateMode(filtersMode) }
    }

    fun updateState(state: FiltersModeButtonState) {
        updateModeInstant(state.mode)
        setDisabled(state.isDisabled)
    }

    fun setOnModeChangeListener(listener: ((FiltersMode) -> Unit)?) {
        onModeChangeListener = listener
    }

    private fun updateMode(currentMode: FiltersMode) {
        val newMode = getNextMode(currentMode)

        animSet?.cancel()
        updateModeInstant(currentMode)

        val oldAppearingWidget = getUnselectedWidget(currentMode)
        val oldDisappearingWidget = getSelectedWidget(currentMode)
        val newAppearingWidget = getSelectedWidget(newMode)
        val newDisappearingWidget = getUnselectedWidget(newMode)

        oldAppearingWidget.alpha = 0f
        oldAppearingWidget.visibility = View.VISIBLE

        newAppearingWidget.alpha = 0f
        newAppearingWidget.visibility = View.VISIBLE

        val duration = 150L
        val hideAnimation = AnimationUtils.getFloatAnimator(
            duration = duration,
            updateListener = {
                oldAppearingWidget.alpha = it
                oldDisappearingWidget.alpha = 1f - it
            },
            completeListener = { oldDisappearingWidget.visibility = View.INVISIBLE }
        )
        val showAnimation = AnimationUtils.getFloatAnimator(
            forward = false,
            duration = duration,
            updateListener = {
                newDisappearingWidget.alpha = it
                newAppearingWidget.alpha = 1f - it
            },
            completeListener = {
                newDisappearingWidget.visibility = View.INVISIBLE
                filtersMode = newMode
                onModeChangeListener?.invoke(newMode)
            }
        )

        animSet = AnimatorSet()
            .apply {
                playSequentially(hideAnimation, showAnimation)
                start()
            }
    }

    private fun getNextMode(currentMode: FiltersMode) =
        when(currentMode) {
            FiltersMode.NO_FILTERS -> FiltersMode.ALL
            FiltersMode.ALL -> FiltersMode.FAVORITE
            FiltersMode.FAVORITE -> FiltersMode.NO_FILTERS
        }

    private fun getSelectedWidget(mode: FiltersMode) =
        when(mode) {
            FiltersMode.NO_FILTERS -> noFiltersSelected
            FiltersMode.ALL -> allFiltersSelected
            FiltersMode.FAVORITE -> favoritesFiltersSelected
        }

    private fun getUnselectedWidget(mode: FiltersMode) =
        when(mode) {
            FiltersMode.NO_FILTERS -> noFilters
            FiltersMode.ALL -> allFilters
            FiltersMode.FAVORITE -> favoritesFilters
        }

    private fun setDisabled(disabled: Boolean) {
        val enabled = !disabled

        super.setEnabled(enabled)

        noFilters.isEnabled = enabled
        noFiltersSelected.isEnabled = enabled
        allFilters.isEnabled = enabled
        allFiltersSelected.isEnabled = enabled
        favoritesFilters.isEnabled = enabled
        favoritesFiltersSelected.isEnabled = enabled
    }

    private fun updateModeInstant(mode: FiltersMode) {
        noFilters.alpha = 1f
        noFiltersSelected.alpha = 1f
        allFilters.alpha = 1f
        allFiltersSelected.alpha = 1f
        favoritesFilters.alpha = 1f
        favoritesFiltersSelected.alpha = 1f

        when(mode) {
            FiltersMode.NO_FILTERS -> {
                noFilters.visibility = View.INVISIBLE
                noFiltersSelected.visibility = View.VISIBLE

                allFilters.visibility = View.VISIBLE
                allFiltersSelected.visibility = View.INVISIBLE

                favoritesFilters.visibility = View.VISIBLE
                favoritesFiltersSelected.visibility = View.INVISIBLE
            }
            FiltersMode.ALL -> {
                noFilters.visibility = View.VISIBLE
                noFiltersSelected.visibility = View.INVISIBLE

                allFilters.visibility = View.INVISIBLE
                allFiltersSelected.visibility = View.VISIBLE

                favoritesFilters.visibility = View.VISIBLE
                favoritesFiltersSelected.visibility = View.INVISIBLE
            }
            FiltersMode.FAVORITE -> {
                noFilters.visibility = View.VISIBLE
                noFiltersSelected.visibility = View.INVISIBLE

                allFilters.visibility = View.VISIBLE
                allFiltersSelected.visibility = View.INVISIBLE

                favoritesFilters.visibility = View.INVISIBLE
                favoritesFiltersSelected.visibility = View.VISIBLE
            }
        }

        filtersMode = mode
    }
}