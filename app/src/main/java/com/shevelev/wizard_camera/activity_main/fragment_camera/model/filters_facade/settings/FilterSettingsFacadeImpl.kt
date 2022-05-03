package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.enums.MappingFilterTexture
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.*
import com.shevelev.wizard_camera.core.database.api.repositories.FilterSettingsDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class FilterSettingsFacadeImpl
constructor(
    private val filterSettingsRepository: FilterSettingsDbRepository
) : FilterSettingsFacade {

    private val settingsMap = mutableMapOf<GlFilterCode, GlFilterSettings>(
        GlFilterCode.ORIGINAL to EmptyFilterSettings(GlFilterCode.ORIGINAL),
        GlFilterCode.PIXELIZE to EmptyFilterSettings(GlFilterCode.PIXELIZE),
        GlFilterCode.BLUE_ORANGE to EmptyFilterSettings(GlFilterCode.BLUE_ORANGE),
        GlFilterCode.BASIC_DEFORM to EmptyFilterSettings(GlFilterCode.BASIC_DEFORM),
        GlFilterCode.CONTRAST to EmptyFilterSettings(GlFilterCode.CONTRAST),
        GlFilterCode.NOISE_WARP to EmptyFilterSettings(GlFilterCode.NOISE_WARP),
        GlFilterCode.REFRACTION to EmptyFilterSettings(GlFilterCode.REFRACTION),
        GlFilterCode.CROSSHATCH to EmptyFilterSettings(GlFilterCode.CROSSHATCH),
        GlFilterCode.ASCII_ART to EmptyFilterSettings(GlFilterCode.ASCII_ART),
        GlFilterCode.MONEY to EmptyFilterSettings(GlFilterCode.MONEY),
        GlFilterCode.POLYGONIZATION to EmptyFilterSettings(GlFilterCode.POLYGONIZATION),
        GlFilterCode.GRAY to EmptyFilterSettings(GlFilterCode.GRAY),
        GlFilterCode.NEGATIVE to EmptyFilterSettings(GlFilterCode.NEGATIVE),
        GlFilterCode.NOSTALGIA to EmptyFilterSettings(GlFilterCode.NOSTALGIA),
        GlFilterCode.CASTING to EmptyFilterSettings(GlFilterCode.CASTING),
        GlFilterCode.RELIEF to EmptyFilterSettings(GlFilterCode.RELIEF),
        GlFilterCode.MIRROR to EmptyFilterSettings(GlFilterCode.MIRROR),
        GlFilterCode.CARTOON to EmptyFilterSettings(GlFilterCode.CARTOON),
        GlFilterCode.WATER_REFLECTION to EmptyFilterSettings(GlFilterCode.WATER_REFLECTION),
        GlFilterCode.REFRACTION to EmptyFilterSettings(GlFilterCode.REFRACTION)
    )

    override suspend fun init() {
        val dbSettings = withContext(Dispatchers.IO) {
            filterSettingsRepository.read()
        }

        settingsMap[GlFilterCode.EDGE_DETECTION] =
            dbSettings.firstOrNull { it.code == GlFilterCode.EDGE_DETECTION }
            ?: EdgeDetectionFilterSettings(isInverted =  false)

        settingsMap[GlFilterCode.BLACK_AND_WHITE] =
            dbSettings.firstOrNull { it.code == GlFilterCode.BLACK_AND_WHITE }
            ?: BlackAndWhiteFilterSettings(isInverted = false)

        settingsMap[GlFilterCode.LEGOFIED] =
            dbSettings.firstOrNull { it.code == GlFilterCode.LEGOFIED }
            ?: LegofiedFilterSettings(size = Size.SMALL)

        settingsMap[GlFilterCode.TRIANGLES_MOSAIC] =
            dbSettings.firstOrNull { it.code == GlFilterCode.TRIANGLES_MOSAIC }
            ?: TrianglesMosaicFilterSettings(size = Size.SMALL)

        settingsMap[GlFilterCode.HEXAGON_MOSAIC] =
            dbSettings.firstOrNull { it.code == GlFilterCode.HEXAGON_MOSAIC }
            ?: HexagonMosaicFilterSettings(size = Size.SMALL)

        settingsMap[GlFilterCode.CRACKED] =
            dbSettings.firstOrNull { it.code == GlFilterCode.CRACKED }
            ?: CrackedFilterSettings(
                shards = 15, 
                randomA = Random.nextDouble(1.0, 359.0).toFloat(),
                randomB = Random.nextDouble(1.0, 359.0).toFloat(),
                randomC = Random.nextDouble(1.0, 359.0).toFloat())

        settingsMap[GlFilterCode.SWIRL] =
            dbSettings.firstOrNull { it.code == GlFilterCode.SWIRL }
                ?: SwirlFilterSettings(radius = 5, rotation = 5, invertRotation = false)

        settingsMap[GlFilterCode.TILE_MOSAIC] =
            dbSettings.firstOrNull { it.code == GlFilterCode.TILE_MOSAIC }
                ?: TileMosaicFilterSettings(tileSize = 70, borderSize = 3)

        settingsMap[GlFilterCode.TRIPLE] =
            dbSettings.firstOrNull { it.code == GlFilterCode.TRIPLE }
                ?: TripleFilterSettings(isHorizontal = true)

        settingsMap[GlFilterCode.NEWSPAPER] =
            dbSettings.firstOrNull { it.code == GlFilterCode.NEWSPAPER }
                ?: NewspaperFilterSettings(isGrayscale = false)

        settingsMap[GlFilterCode.MAPPING] =
            dbSettings.firstOrNull { it.code == GlFilterCode.MAPPING }
                ?: MappingFilterSettings(texture = MappingFilterTexture.TEXTURE_0, mixFactor = 15)
    }

    override fun get(code: GlFilterCode): GlFilterSettings = settingsMap[code]!!

    override suspend fun update(settings: GlFilterSettings) {
        withContext(Dispatchers.IO) {
            filterSettingsRepository.update(settings)
        }

        settingsMap[settings.code] = settings
    }
}