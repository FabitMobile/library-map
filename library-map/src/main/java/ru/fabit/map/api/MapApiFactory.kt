package ru.fabit.map.api

import ru.fabit.map.api.MapApi
import ru.fabit.map.api.internal.MapApiImpl
import ru.fabit.map.internal.protocol.MapProtocol

class MapApiFactory {

    companion object {
        fun create(
            mapProtocol: MapProtocol,
            getCurrentTime: () -> Long,
            cleanParkOutDate: (ids: List<Int>?) -> Unit,
            settings: MapApiSettings
        ): MapApi {
            return MapApiImpl(
                mapProtocol,
                getCurrentTime,
                cleanParkOutDate,
                settings
            )
        }
    }
}