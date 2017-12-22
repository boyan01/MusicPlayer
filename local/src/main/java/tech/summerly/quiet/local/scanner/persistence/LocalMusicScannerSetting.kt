package tech.summerly.quiet.local.scanner.persistence

import android.content.Context
import tech.summerly.quiet.commonlib.utils.edit

/**
 * author : Summer
 * date   : 2017/10/20
 */
class LocalMusicScannerSetting(context: Context) : ILocalMusicScannerSetting {

    companion object {
        private const val NAME = "preference_local_music_scanner"

        private const val KEY_IS_FILTER_BY_DURATION = "isFilterByDuration"

        private const val KEY_LIMIT_DURATION = "limitDuration"

        private const val KEY_FILTER_FOLDERS = "filterFolders"

        private const val DEFAULT_LIMIT_DURATION = 1000 * 30
    }

    private val preference by lazy {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun setFilterByDuration(filter: Boolean) {
        preference.edit {
            putBoolean(KEY_IS_FILTER_BY_DURATION, filter)
        }
    }

    override fun isFilterByDuration(): Boolean {
        return preference.getBoolean(KEY_IS_FILTER_BY_DURATION, true)
    }

    fun setLimitDuration(duration: Int) {
        preference.edit {
            putInt(KEY_LIMIT_DURATION, duration)
        }
    }

    override fun getLimitDuration(): Int {
        return preference.getInt(KEY_LIMIT_DURATION, DEFAULT_LIMIT_DURATION)
    }

    fun addFilterFolder(folderPath: String) {
        preference.edit {
            val folders = preference.getStringSet(KEY_FILTER_FOLDERS, null) ?: HashSet<String>()
            folders.add(folderPath)
            putStringSet(KEY_FILTER_FOLDERS, folders)
        }
    }

    fun removeFilterFolderByPath(folderPath: String) {
        preference.edit {
            val folders = preference.getStringSet(KEY_FILTER_FOLDERS, null) ?: HashSet<String>()
            folders.remove(folderPath)
            putStringSet(KEY_FILTER_FOLDERS, folders)
        }
    }

    override fun getAllFilterFolder(): Set<String> {
        return preference.getStringSet(KEY_FILTER_FOLDERS, null) ?: emptySet()
    }

}

