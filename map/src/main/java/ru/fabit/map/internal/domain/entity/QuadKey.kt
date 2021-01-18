package ru.fabit.map.internal.domain.entity

class QuadKey(val x: Int, val y: Int, val z: Int) {

    override fun toString(): String {
        var result = ""
        for (i in z downTo 0) {
            var digit = 0
            val mask = 1 shl (i - 1)
            if ((x and mask) != 0) {
                digit++
            }
            if ((y and mask) != 0) {
                digit += 2
            }
            result = result.plus(digit)
        }
        return result
    }
}