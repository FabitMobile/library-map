package ru.fabit.map.dependencies.provider

interface MapStyleProvider {
    fun getDefaultStyle(): String
    fun getCongestionStyle(): String
}