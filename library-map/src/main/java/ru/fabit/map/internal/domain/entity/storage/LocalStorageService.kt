package ru.fabit.map.internal.domain.entity.storage

interface LocalStorageService {

    fun getData(key: String, defaultValue: String): String

    fun saveData(key: String, value: String)

    fun remove(key: String)

    fun <K, V>getMap(key: String, keyClass: Class<*>, valueClass: Class<*>): HashMap<K, V>

    fun <K, V>setMap(key: String, map: HashMap<K, V>)
}