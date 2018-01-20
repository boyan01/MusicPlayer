package tech.summerly.quiet.local.scanner.persistence

import android.content.Context
import tech.summerly.quiet.commonlib.utils.edit
import java.io.File

/**
 * author : Summer
 * date   : 2017/10/20
 */
internal class ScannerSetting(context: Context) : IScannerSetting {

    companion object {
        private const val NAME = "preference_local_music_scanner"

        const val KEY_FILTER_DURATION = "isFilterByDuration"

        const val KEY_FILTER_SIZE = "isFilterBySize"

        private const val DEFAULT_LIMIT_DURATION = 1000 * 30

        const val SIZE_MAX: Long = 500 * 1024
    }

    private val preference by lazy {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    override fun isFilterByDuration(): Boolean {
        return preference.getBoolean(KEY_FILTER_DURATION, true)
    }


    override fun put(key: String, value: Boolean) {
        preference.edit {
            putBoolean(key, value)
        }
    }

    override fun getLimitDuration(): Int {
        return DEFAULT_LIMIT_DURATION
    }

    private fun removeFolder(path: String) {
        preference.edit {
            remove(path)
        }
    }

    override fun getAllFilterFolder(): Set<String> {
        return getAllFolder().filterValues { !it }.keys
    }

    @Suppress("UNCHECKED_CAST")
    fun getAllFolder(): Map<String, Boolean> {
        val all = preference.all
        val folders = all.filterKeys {
            it != KEY_FILTER_DURATION && it != KEY_FILTER_SIZE
        } as Map<String, Boolean>
        return folders.filter {
            val exists = File(it.key).exists()
            if (!exists) {
                removeFolder(it.key)
            }
            exists
        }
    }

    operator fun get(key: String): Boolean {
        return preference.getBoolean(key, false)
    }

}

