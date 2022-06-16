package com.shevelev.wizard_camera.feature.filters_facade.api.di

import org.koin.core.scope.ScopeID

data class FiltersFacadeInjectionSettings(
    val scopeId: ScopeID,
    val useInMemoryLastUsedFilters: Boolean,
    val canUpdateFavorites: Boolean
)