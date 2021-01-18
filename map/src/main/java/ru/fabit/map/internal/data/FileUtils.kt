package ru.fabit.map.internal.data

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


fun jsonResource(context: Context, resourceName: String): String {
    val builder = StringBuilder()
    val resourceIdentifier = context.resources.getIdentifier(resourceName, "raw", context?.packageName)
    val inputStream: InputStream = context.resources.openRawResource(resourceIdentifier)
    val reader = BufferedReader(InputStreamReader(inputStream))
    try {
        reader.lineSequence().forEach {
            builder.append(it)
        }
    } catch (ex: IOException) {
        reader.close()
        throw ex
    }
    return builder.toString()
}