package tech.summerly.quiet.commonlib.utils

import android.content.SharedPreferences

/**
 * Created by summer on 17-12-17
 */
inline fun SharedPreferences.edit(func: (SharedPreferences.Editor.() -> Unit)) =
        edit().apply {
            func()
        }.apply()