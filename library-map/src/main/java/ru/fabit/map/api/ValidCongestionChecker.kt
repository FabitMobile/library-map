package ru.fabit.map.api

interface ValidCongestionChecker {
    fun checkValidCongestion(percentFreeSpaces: Int?): Boolean
}