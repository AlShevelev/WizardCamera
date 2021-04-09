package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.enums.MappingFilterTexture
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.*
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.storage.repositories.FilterSettingsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

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
        FilterCode.BLUE_ORANGE to EmptyFilterSettings(FilterCode.BLUE_ORANGE),
        FilterCode.CHROMATIC_ABERRATION to EmptyFilterSettings(FilterCode.CHROMATIC_ABERRATION),
        FilterCode.BASIC_DEFORM to EmptyFilterSettings(FilterCode.BASIC_DEFORM),
        FilterCode.CONTRAST to EmptyFilterSettings(FilterCode.CONTRAST),
        FilterCode.NOISE_WARP to EmptyFilterSettings(FilterCode.NOISE_WARP),
        FilterCode.REFRACTION to EmptyFilterSettings(FilterCode.REFRACTION),
        FilterCode.CROSSHATCH to EmptyFilterSettings(FilterCode.CROSSHATCH),
        FilterCode.ASCII_ART to EmptyFilterSettings(FilterCode.ASCII_ART),
        FilterCode.MONEY to EmptyFilterSettings(FilterCode.MONEY),
        FilterCode.POLYGONIZATION to EmptyFilterSettings(FilterCode.POLYGONIZATION),
        FilterCode.GRAY to EmptyFilterSettings(FilterCode.GRAY),
        FilterCode.NEGATIVE to EmptyFilterSettings(FilterCode.NEGATIVE),
        FilterCode.NOSTALGIA to EmptyFilterSettings(FilterCode.NOSTALGIA),
        FilterCode.CASTING to EmptyFilterSettings(FilterCode.CASTING),
        FilterCode.RELIEF to EmptyFilterSettings(FilterCode.RELIEF),
        FilterCode.MIRROR to EmptyFilterSettings(FilterCode.MIRROR),
        FilterCode.CARTOON to EmptyFilterSettings(FilterCode.CARTOON),
        FilterCode.WATER_REFLECTION to EmptyFilterSettings(FilterCode.WATER_REFLECTION)
    )

    override suspend fun init() {
        val dbSettings = withContext(dispatchersProvider.ioDispatcher) {
            filterSettingsRepository.read()
        }

        settingsMap[FilterCode.EDGE_DETECTION] =
            dbSettings.firstOrNull { it.code == FilterCode.EDGE_DETECTION } 
            ?: EdgeDetectionFilterSettings(isInverted =  false)

        settingsMap[FilterCode.BLACK_AND_WHITE] =
            dbSettings.firstOrNull { it.code == FilterCode.BLACK_AND_WHITE } 
            ?: BlackAndWhiteFilterSettings(isInverted = false)

        settingsMap[FilterCode.LEGOFIED] =
            dbSettings.firstOrNull { it.code == FilterCode.LEGOFIED } 
            ?: LegofiedFilterSettings(size = Size.SMALL)

        settingsMap[FilterCode.TRIANGLES_MOSAIC] =
            dbSettings.firstOrNull { it.code == FilterCode.TRIANGLES_MOSAIC } 
            ?: TrianglesMosaicFilterSettings(size = Size.SMALL)

        settingsMap[FilterCode.HEXAGON_MOSAIC] =
            dbSettings.firstOrNull { it.code == FilterCode.HEXAGON_MOSAIC } 
            ?: HexagonMosaicFilterSettings(size = Size.SMALL)

        settingsMap[FilterCode.CRACKED] =
            dbSettings.firstOrNull { it.code == FilterCode.CRACKED } 
            ?: CrackedFilterSettings(
                shards = 15, 
                randomA = Random.nextDouble(1.0, 359.0).toFloat(),
                randomB = Random.nextDouble(1.0, 359.0).toFloat(),
                randomC = Random.nextDouble(1.0, 359.0).toFloat())

        settingsMap[FilterCode.SWIRL] =
            dbSettings.firstOrNull { it.code == FilterCode.SWIRL }
                ?: SwirlFilterSettings(radius = 5, rotation = 5, invertRotation = false)

        settingsMap[FilterCode.TILE_MOSAIC] =
            dbSettings.firstOrNull { it.code == FilterCode.TILE_MOSAIC }
                ?: TileMosaicFilterSettings(tileSize = 70, borderSize = 3)

        settingsMap[FilterCode.TRIPLE] =
            dbSettings.firstOrNull { it.code == FilterCode.TRIPLE }
                ?: TripleFilterSettings(isHorizontal = true)

        settingsMap[FilterCode.NEWSPAPER] =
            dbSettings.firstOrNull { it.code == FilterCode.NEWSPAPER }
                ?: NewspaperFilterSettings(isGrayscale = false)

        settingsMap[FilterCode.MAPPING] =
            dbSettings.firstOrNull { it.code == FilterCode.MAPPING }
                ?: MappingFilterSettings(texture = MappingFilterTexture.TEXTURE_0, mixFactor = 15)
    }

    override fun get(code: FilterCode): FilterSettings = settingsMap[code]!!

    override suspend fun update(settings: FilterSettings) {
        withContext(dispatchersProvider.ioDispatcher) {
            filterSettingsRepository.update(settings)
        }

        settingsMap[settings.code] = settings
    }
}