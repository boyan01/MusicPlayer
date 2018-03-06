package tech.summerly.quiet.search.utils

import com.snappydb.DB
import com.snappydb.DBFactory
import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.search.SearchModule
import tech.summerly.quiet.search.fragments.items.History

/**
 * Created by summer on 18-3-6
 */
private const val SEARCH_DB_NAME = "search"

private const val KEY_HISTORY = "histories"

private fun getSearchDb(): DB {
    return DBFactory.open(SearchModule, SEARCH_DB_NAME)
}

private suspend fun runWithSearchDb(action: DB.() -> Unit) = async {
    val db = getSearchDb()
    try {
        db.action()
    } catch (e: Exception) {
        log(LoggerLevel.ERROR) { e.printStackTrace(); "run with search db 出错！ $action" }
    } finally {
        db.close()
    }
}.await()

internal suspend fun saveHistory(histories: Array<History>) = runWithSearchDb {
    put(KEY_HISTORY, histories)
}

internal suspend fun getHistory(): Array<History> = async {
    val db = getSearchDb()
    try {
        return@async db.getObjectArray(KEY_HISTORY, History::class.java)
    } catch (e: Exception) {
        log { e.printStackTrace() }
        if (db.exists(KEY_HISTORY)) {
            db.del(KEY_HISTORY)
        }
    } finally {
        db.close()
    }
    return@async arrayOf<History>()
}.await()