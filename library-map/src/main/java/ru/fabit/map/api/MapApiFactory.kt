package ru.fabit.map.api

import ru.fabit.map.api.internal.MapApiImpl
import ru.fabit.map.internal.protocol.MapProtocol

class MapApiFactory {

    companion object {
        fun create(
            mapProtocol: MapProtocol,
            getCurrentTime: () -> Long,
            cleanParkOutDate: (ids: List<Int>?) -> Unit,
            settings: MapApiSettings,
            validCongestionChecker: ValidCongestionChecker
        ): MapApi {
            return MapApiImpl(
                mapProtocol,
                getCurrentTime,
                cleanParkOutDate,
                settings,
                validCongestionChecker
            )
        }
    }
}