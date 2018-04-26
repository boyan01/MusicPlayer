package tech.summerly.quiet.search.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import tech.summerly.quiet.commonlib.utils.getObject
import tech.summerly.quiet.commonlib.utils.putObject
import tech.summerly.quiet.search.SearchModule
import tech.summerly.quiet.search.fragments.items.History

/**
 * Created by summer on 18-3-6
 */
private const val SEARCH_PREF_NAME = "search"

private const val KEY_HISTORY = "histories"


//搜索的一些配置选项
private fun getPreference(): SharedPreferences {
    return SearchModule.getSharedPreferences(SEARCH_PREF_NAME, Context.MODE_PRIVATE)
}

/**
 * 更新搜索历史记录
 */
internal fun saveHistory(histories: List<History>) = getPreference().edit {
    putObject(KEY_HISTORY, histories)
}

/**
 * 获取搜索历史记录
 */
internal fun getHistory(): List<History> {

    return getPreference().getObject<List<History>>(KEY_HISTORY) ?: emptyList()

}