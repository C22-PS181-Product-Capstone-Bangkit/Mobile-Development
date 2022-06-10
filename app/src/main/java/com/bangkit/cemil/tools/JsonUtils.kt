package com.bangkit.cemil.tools

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonUtils {

    inline fun <reified T : Any> String?.fromJson(gson: Gson): T? = this?.let {
        val typeToken = object : TypeToken<T>() {}.type
        gson.fromJson(this, typeToken)
    }

}