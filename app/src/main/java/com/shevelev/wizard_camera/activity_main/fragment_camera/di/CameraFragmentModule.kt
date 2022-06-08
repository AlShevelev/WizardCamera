package com.shevelev.wizard_camera.activity_main.fragment_camera.di

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractor
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractorImpl
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import com.shevelev.wizard_camera.feature.filters_facade.impl.FiltersFacadeImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFiltersImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacadeImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCaptureImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManagerImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.view_model.CameraFragmentViewModel
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.groups.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

private const val SCOPE_ID = "CAMERA_FRAGMENT_SCOPE_ID"
private const val SCOPE = "CAMERA_FRAGMENT_SCOPE"

object CameraFragmentScope {
    fun getScope() = KoinJavaComponent.getKoin().getOrCreateScope(SCOPE_ID, named(SCOPE))
    fun closeScope() = KoinJavaComponent.getKoin().getScopeOrNull(SCOPE_ID)?.close()
}

val CameraFragmentModule = module(createdAtStart = false) {
    scope(named(SCOPE)) {
        scoped<LastUsedFilters> {
            LastUsedFiltersImpl(
                lastUsedFilterRepository = CameraFragmentScope.getScope().get()
            )
        }

        scoped<FilterSettingsFacade> {
            FilterSettingsFacadeImpl(
                filterSettingsRepository = get()
            )
        }

        scoped<FiltersGroupStorage>(named(FiltersGroup.FAVORITES)) {
            FavoriteFiltersGroupStorage(
                favoriteFilterRepository = get(),
                displayData = get(),
                filterSettings = CameraFragmentScope.getScope().get(),
                lastUsedFilters = CameraFragmentScope.getScope().get(),
            )
        }
    }

    single<FilterDisplayDataList> {
        FilterDisplayDataListImpl()
    }

    factory<OrientationManager> {
        OrientationManagerImpl(
            context = get()
        )
    }

    factory<ImageCapture> {
        ImageCaptureImpl(
            photoShotRepository = get(),
            appContext = get()
        )
    }

    factory<FiltersFacade> {
        FiltersFacadeImpl(
            context = get(),
            lastUsedFilters = CameraFragmentScope.getScope().get(),
            displayData = get(),
            filterSettings = CameraFragmentScope.getScope().get(),
            groups = mapOf(
                FiltersGroup.ALL to get(named(FiltersGroup.ALL)),
                FiltersGroup.COLORS to get(named(FiltersGroup.COLORS)),
                FiltersGroup.DEFORMATIONS to get(named(FiltersGroup.DEFORMATIONS)),
                FiltersGroup.FAVORITES to CameraFragmentScope.getScope().get(named(FiltersGroup.FAVORITES)),
                FiltersGroup.NO_FILTERS to get(named(FiltersGroup.NO_FILTERS)),
                FiltersGroup.STYLIZATION to get(named(FiltersGroup.STYLIZATION)),
            )
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.ALL)) {
        AllFiltersGroupStorage(
            favoriteFilters = CameraFragmentScope.getScope().get(named(FiltersGroup.FAVORITES)),
            displayData = get(),
            filterSettings = CameraFragmentScope.getScope().get(),
            lastUsedFilters = CameraFragmentScope.getScope().get(),
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.COLORS)) {
        ColorsFilterGroupStorage(
            favoriteFilters = CameraFragmentScope.getScope().get(named(FiltersGroup.FAVORITES)),
            displayData = get(),
            filterSettings = CameraFragmentScope.getScope().get(),
            lastUsedFilters = CameraFragmentScope.getScope().get(),
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.DEFORMATIONS)) {
        DeformationsFiltersGroupStorage(
            favoriteFilters = CameraFragmentScope.getScope().get(named(FiltersGroup.FAVORITES)),
            displayData = get(),
            filterSettings = CameraFragmentScope.getScope().get(),
            lastUsedFilters = CameraFragmentScope.getScope().get(),
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.NO_FILTERS)) {
        NoFiltersGroupStorage()
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.STYLIZATION)) {
        StylizationFilterGroupStorage(
            favoriteFilters = CameraFragmentScope.getScope().get(named(FiltersGroup.FAVORITES)),
            displayData = get(),
            filterSettings = CameraFragmentScope.getScope().get(),
            lastUsedFilters = CameraFragmentScope.getScope().get(),
        )
    }

    factory<CameraFragmentInteractor> {
        CameraFragmentInteractorImpl(
            filters = get(),
            capture = get(),
            orientation = get()
        )
    }

    viewModel {
        CameraFragmentViewModel(
            appContext = get(),
            interactor = get()
        )
    }
}