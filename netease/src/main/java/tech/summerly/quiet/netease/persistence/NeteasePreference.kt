package tech.summerly.quiet.netease.persistence

import android.content.Context
import com.google.gson.Gson
import tech.summerly.quiet.commonlib.AppContext
import tech.summerly.quiet.commonlib.utils.edit
import tech.summerly.quiet.commonlib.utils.fromJson
import tech.summerly.quiet.netease.api.result.LoginResultBean

internal object NeteasePreference {

    private val context: Context
        get() = AppContext.instance

    private const val prefix = "netease_"

    private const val NAME_USER = prefix + "user"

    private const val KEY_USER = prefix + "user_id"

    fun getLoginUser(): LoginResultBean.Profile? {
        val preferences = context.getSharedPreferences(NAME_USER, Context.MODE_PRIVATE)
        val json = preferences.getString(KEY_USER, "")
        return Gson().fromJson(json)
    }

    fun saveLoginUser(profile: LoginResultBean.Profile) {
        val preferences = context.getSharedPreferences(NAME_USER, Context.MODE_PRIVATE)
        preferences.edit {
            putString(KEY_USER, Gson().toJson(profile))
        }
    }

}