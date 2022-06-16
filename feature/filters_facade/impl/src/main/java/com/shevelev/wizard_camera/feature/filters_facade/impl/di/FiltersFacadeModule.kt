package com.shevelev.wizard_camera.feature.filters_facade.impl.di

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import com.shevelev.wizard_camera.feature.filters_facade.api.di.FiltersFacadeInjectionSettings
import com.shevelev.wizard_camera.feature.filters_facade.impl.FiltersFacadeImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.groups.*
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFiltersImpl
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFiltersInMemoryImpl
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
        scoped<LastUsedFilters> { (settings: FiltersFacadeInjectionSettings) ->
            if(settings.useInMemoryLastUsedFilters) {
                LastUsedFiltersInMemoryImpl()
            } else {
                LastUsedFiltersImpl(
                    lastUsedFilterRepository = get()
                )
            }
        }

        scoped<FilterSettingsFacade> {
            FilterSettingsFacadeImpl(
                filterSettingsRepository = get()
            )
        }

        scoped<FiltersGroupStorage>(named(FiltersGroup.FAVORITES)) { (settings: FiltersFacadeInjectionSettings) ->
            FavoriteFiltersGroupStorage(
                favoriteFilterRepository = get(),
                displayData = get(),
                filterSettings = FiltersFacadeScope.get(settings.scopeId).get(),
                lastUsedFilters = FiltersFacadeScope.get(settings.scopeId).get(parameters = { parametersOf(settings) }),
            )
        }

        scoped<FiltersFacade> { (settings: FiltersFacadeInjectionSettings) ->
            FiltersFacadeImpl(
                context = get(),
                lastUsedFilters = FiltersFacadeScope.get(settings.scopeId).get(parameters = { parametersOf(settings) }),
                displayData = get(),
                filterSettings = FiltersFacadeScope.get(settings.scopeId).get(),
                groups = mapOf(
                    FiltersGroup.ALL to get(
                        named(FiltersGroup.ALL),
                        parameters = { parametersOf(settings) }
                    ),
                    FiltersGroup.COLORS to get(
                        named(FiltersGroup.COLORS),
                        parameters = { parametersOf(settings) }
                    ),
                    FiltersGroup.DEFORMATIONS to get(
                        named(FiltersGroup.DEFORMATIONS),
                        parameters = { parametersOf(settings) }
                    ),
                    FiltersGroup.FAVORITES to FiltersFacadeScope.get(settings.scopeId).get(
                        named(FiltersGroup.FAVORITES),
                        parameters = { parametersOf(settings) }
                    ),
                    FiltersGroup.NO_FILTERS to get(named(FiltersGroup.NO_FILTERS)),
                    FiltersGroup.STYLIZATION to get(
                        named(FiltersGroup.STYLIZATION),
                        parameters = { parametersOf(settings) }
                    ),
                )
            )
        }
    }

    single<FilterDisplayDataList> {
        FilterDisplayDataListImpl()
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.ALL)) { (settings: FiltersFacadeInjectionSettings) ->
        AllFiltersGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(settings.scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(settings) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(settings.scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(settings.scopeId).get(parameters = { parametersOf(settings) }),
            canUpdateFavorites = settings.canUpdateFavorites
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.COLORS)) { (settings: FiltersFacadeInjectionSettings) ->
        ColorsFilterGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(settings.scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(settings) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(settings.scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(settings.scopeId).get(parameters = { parametersOf(settings) }),
            canUpdateFavorites = settings.canUpdateFavorites
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.DEFORMATIONS)) { (settings: FiltersFacadeInjectionSettings) ->
        DeformationsFiltersGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(settings.scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(settings) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(settings.scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(settings.scopeId).get(parameters = { parametersOf(settings) }),
            canUpdateFavorites = settings.canUpdateFavorites
        )
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.NO_FILTERS)) {
        NoFiltersGroupStorage()
    }

    factory<FiltersGroupStorage>(named(FiltersGroup.STYLIZATION)) { (settings: FiltersFacadeInjectionSettings) ->
        StylizationFilterGroupStorage(
            favoriteFilters = FiltersFacadeScope.get(settings.scopeId).get(
                named(FiltersGroup.FAVORITES),
                parameters = { parametersOf(settings) }
            ),
            displayData = get(),
            filterSettings = FiltersFacadeScope.get(settings.scopeId).get(),
            lastUsedFilters = FiltersFacadeScope.get(settings.scopeId).get(parameters = { parametersOf(settings) }),
            canUpdateFavorites = settings.canUpdateFavorites
        )
    }
}