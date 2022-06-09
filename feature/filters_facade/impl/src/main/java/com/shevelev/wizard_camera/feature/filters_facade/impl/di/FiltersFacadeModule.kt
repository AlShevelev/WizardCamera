package com.shevelev.wizard_camera.feature.filters_facade.impl.di

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import com.shevelev.wizard_camera.feature.filters_facade.impl.FiltersFacadeImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.groups.*
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFiltersImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacadeImpl
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.ScopeID
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

private const val SCOPE_NAME = "FILTERS_FACADE_SCOPE"

object FiltersFacadeScope {
    fun get(scopeId: ScopeID) = getKoin().getOrCreateScope(scopeId, named(SCOPE_NAME))
    fun close(scopeId: ScopeID) = getKoin().getScopeOrNull(scopeId)?.close()
}

val FiltersFacadeModule = module(createdAtStart = false) {
    scope(named(SCOPE_NAME)) {
        scoped<LastUsedFilters> { (scopeId: ScopeID) ->
            LastUsedFiltersImpl(
                lastUsedFilterRepository = FiltersFacadeScope.get(scopeId).get()
            )
        }

        scoped<FilterSettingsFacade> {
            FilterSettingsFacadeImpl(
                filterSettingsRepository = get()
            )
        }

        scoped<FiltersGroupStorage>(named(FiltersGroup.FAVORITES)) { (scopeId: ScopeID) ->
            FavoriteFiltersGroupStorage(
                favoriteFilterRepository = get(),
                displayData = get(),
                filterSettings = FiltersFacadeScope.get(scopeId).get(),
                lastUsedFilters = FiltersFacadeScope.get(scopeId).get(parameters = { parametersOf(scopeId) }),
            )
        }
    }

    single<FilterDisplayDataList> {
        FilterDisplayDataListImpl()
    }

    factory<FiltersFacade> { (scopeId: ScopeID) ->
        FiltersFacadeImpl(
            context = get(),
            lastUsedFilters = FiltersFacadeScope.get(scopeId).get(parameters = { parametersOf(scopeId) }),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(scopeId).get(),
            groups = mapOf(
                FiltersGroup.ALL to get(
                    named(FiltersGroup.ALL),
                    parameters = { parametersOf(scopeId) }
                ),
                FiltersGroup.COLORS to get(
                    named(FiltersGroup.COLORS),
                    parameters = { parametersOf(scopeId) }
                ),
                FiltersGroup.DEFORMATIONS to get(
                    named(FiltersGroup.DEFORMATIONS),
                    parameters = { parametersOf(scopeId) }
                ),
                FiltersGroup.FAVORITES to FiltersFacadeScope.get(scopeId).get(
                    named(FiltersGroup.FAVORITES),
                    parameters = { parametersOf(scopeId) }
                ),
                FiltersGroup.NO_FILTERS to get(named(FiltersGroup.NO_FILTERS)),
                FiltersGroup.STYLIZATION to get(
                    named(FiltersGroup.STYLIZATION),
                    parameters = { parametersOf(scopeId) }
                ),
            )
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.ALL)) { (scopeId: ScopeID) ->
        AllFiltersGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(scopeId) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(scopeId).get(parameters = { parametersOf(scopeId) }),
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.COLORS)) { (scopeId: ScopeID) ->
        ColorsFilterGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(scopeId) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(scopeId).get(parameters = { parametersOf(scopeId) }),
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.DEFORMATIONS)) { (scopeId: ScopeID) ->
        DeformationsFiltersGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(scopeId) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(scopeId).get(parameters = { parametersOf(scopeId) }),
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.NO_FILTERS)) {
        NoFiltersGroupStorage()
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.STYLIZATION)) { (scopeId: ScopeID) ->
        StylizationFilterGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(scopeId) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(scopeId).get(parameters = { parametersOf(scopeId) }),
        )
    }
}