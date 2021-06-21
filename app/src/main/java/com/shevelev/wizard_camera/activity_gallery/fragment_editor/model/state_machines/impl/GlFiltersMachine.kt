package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.filters_ui.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FiltersListData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

class GlFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider,
    editorStorage: EditorStorage,
    private val filterDisplayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade
) : EditorMachineBase(outputCommands, dispatchersProvider, editorStorage) {

    private var isFilterSettingsFacadeSetUp = false

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                if(!isFilterSettingsFacadeSetUp) {
                    filterSettings.init()
                    isFilterSettingsFacadeSetUp = true

                    outputCommands.emit(IntiGlFilterCarousel(getFiltersListData()))
                }

                outputCommands.emit(SelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(SetInitialImage(editorStorage.image,  editorStorage.currentFilter))
                delay(150L)         // To avoid the carousel's flickering
                outputCommands.emit(ShowGlFilterCarousel)
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterCarousel)
                State.NO_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.SYSTEM_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterCarousel)
                State.SYSTEM_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterCarousel)
                State.CROP
            }

            state == State.MAIN && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.MAIN && event is GlFilterSettingsShown -> {
                outputCommands.emit(HideGlFilterCarousel)
                // todo outputCommands.emit(ShowGlFilterSettings(...))      // Show settings
                State.SETTINGS_VISIBLE
            }

            state == State.MAIN && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is GlFilterSwitched -> {
                val filter = editorStorage.getUsedFilter(event.filterId.filterCode) ?: filterSettings[event.filterId.filterCode]

                editorStorage.currentFilter = filter
                outputCommands.emit(UpdateImageByGlFilter(filter))

                state
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterSettings)
                State.NO_FILTERS
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.SYSTEM_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterSettings)
                State.SYSTEM_FILTERS
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterSettings)
                State.CROP
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsHided -> {
                outputCommands.emit(HideGlFilterSettings)
                outputCommands.emit(ShowGlFilterCarousel)
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.SETTINGS_VISIBLE && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsUpdated -> {
                // todo Memorize the settings
                // todo Update an image
                state
            }

            else -> state
        }

    private fun getFiltersListData() : FiltersListData {
        val startItems = filterDisplayData.map {
            FilterListItem(it, FilterFavoriteType.HIDDEN, filterSettings[it.id.filterCode] !is EmptyFilterSettings)
        }

        return FiltersListData(filterDisplayData.getIndex(editorStorage.currentFilter.code), startItems)
    }
}