package tech.summerly.quiet.commonlib.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by summer on 17-12-25
 */
inline fun <reified T : Any> Gson.fromJson(json: String?): T? {
    json ?: return null
    return fromJson<T>(json, object : TypeToken<T>() {}.type)
}