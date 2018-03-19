package tech.summerly.quiet.commonlib.utils

import android.content.SharedPreferences
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by summer on 18-3-18
 */


fun FragmentManager.intransaction(action: FragmentTransaction.() -> Unit) {
    val transaction = beginTransaction()
    try {
        action(transaction)
    } finally {
        transaction.commit()
    }
}


fun SharedPreferences.Editor.putObject(key: String, value: Any?) {
    if (value == null) {
        putString(key, value)
    } else {
        putString(key, Gson().toJson(value))
    }
}


inline fun <reified T> SharedPreferences.getObject(key: String): T? {
    val value = getString(key, null)
    if (value.isNullOrEmpty()) {
        return null
    }
    val type = object : TypeToken<T>() {}.type
    return try {
        Gson().fromJson<T>(value, type)
    } catch (e: Exception) {
        log(LoggerLevel.ERROR) { e.printStackTrace() }
        null
    }
}