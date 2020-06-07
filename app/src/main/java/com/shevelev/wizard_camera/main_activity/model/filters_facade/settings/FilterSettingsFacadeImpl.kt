package com.shevelev.wizard_camera.main_activity.model.filters_facade.settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.EdgeDetectionFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.EmptyFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.storage.repositories.FilterSettingsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilterSettingsFacadeImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val filterSettingsRepository: FilterSettingsRepository
) : FilterSettingsFacade {

    private val settingsMap = mutableMapOf<FilterCode, FilterSettings>(
        FilterCode.ORIGINAL to EmptyFilterSettings(FilterCode.ORIGINAL),
        FilterCode.PIXELIZE to EmptyFilterSettings(FilterCode.PIXELIZE),
        FilterCode.EM_INTERFERENCE to EmptyFilterSettings(FilterCode.EM_INTERFERENCE),
        FilterCode.TRIANGLES_MOSAIC to EmptyFilterSettings(FilterCode.TRIANGLES_MOSAIC),
        FilterCode.LEGOFIED to EmptyFilterSettings(FilterCode.LEGOFIED),
        FilterCode.TILE_MOSAIC to EmptyFilterSettings(FilterCode.TILE_MOSAIC),
        FilterCode.BLUE_ORANGE to EmptyFilterSettings(FilterCode.BLUE_ORANGE),
        FilterCode.CHROMATIC_ABERRATION to EmptyFilterSettings(FilterCode.CHROMATIC_ABERRATION),
        FilterCode.BASIC_DEFORM to EmptyFilterSettings(FilterCode.BASIC_DEFORM),
        FilterCode.CONTRAST to EmptyFilterSettings(FilterCode.CONTRAST),
        FilterCode.NOISE_WARP to EmptyFilterSettings(FilterCode.NOISE_WARP),
        FilterCode.REFRACTION to EmptyFilterSettings(FilterCode.REFRACTION),
        FilterCode.MAPPING to EmptyFilterSettings(FilterCode.MAPPING),
        FilterCode.CROSSHATCH to EmptyFilterSettings(FilterCode.CROSSHATCH),
        FilterCode.LICHTENSTEIN_ESQUE to EmptyFilterSettings(FilterCode.LICHTENSTEIN_ESQUE),
        FilterCode.ASCII_ART to EmptyFilterSettings(FilterCode.ASCII_ART),
        FilterCode.MONEY to EmptyFilterSettings(FilterCode.MONEY),
        FilterCode.CRACKED to EmptyFilterSettings(FilterCode.CRACKED),
        FilterCode.POLYGONIZATION to EmptyFilterSettings(FilterCode.POLYGONIZATION),
        FilterCode.BLACK_AND_WHITE to EmptyFilterSettings(FilterCode.BLACK_AND_WHITE),
        FilterCode.GRAY to EmptyFilterSettings(FilterCode.GRAY),
        FilterCode.NEGATIVE to EmptyFilterSettings(FilterCode.NEGATIVE),
        FilterCode.NOSTALGIA to EmptyFilterSettings(FilterCode.NOSTALGIA),
        FilterCode.CASTING to EmptyFilterSettings(FilterCode.CASTING),
        FilterCode.RELIEF to EmptyFilterSettings(FilterCode.RELIEF),
        FilterCode.SWIRL to EmptyFilterSettings(FilterCode.SWIRL),
        FilterCode.HEXAGON_MOSAIC to EmptyFilterSettings(FilterCode.HEXAGON_MOSAIC),
        FilterCode.MIRROR to EmptyFilterSettings(FilterCode.MIRROR),
        FilterCode.TRIPLE to EmptyFilterSettings(FilterCode.TRIPLE),
        FilterCode.CARTOON to EmptyFilterSettings(FilterCode.CARTOON),
        FilterCode.WATER_REFLECTION to EmptyFilterSettings(FilterCode.WATER_REFLECTION)
    )

    override suspend fun init() {
        val dbSettings = withContext(dispatchersProvider.ioDispatcher) {
            filterSettingsRepository.read()
        }

        settingsMap[FilterCode.EDGE_DETECTION] =
            dbSettings.firstOrNull { it.code == FilterCode.EDGE_DETECTION } ?:
            EdgeDetectionFilterSettings(FilterCode.EDGE_DETECTION, false)
    }

    override fun get(code: FilterCode): FilterSettings = settingsMap[code]!!

    override suspend fun update(settings: FilterSettings) {
        withContext(dispatchersProvider.ioDispatcher) {
            filterSettingsRepository.update(settings)
        }

        settingsMap[settings.code] = settings
    }
}