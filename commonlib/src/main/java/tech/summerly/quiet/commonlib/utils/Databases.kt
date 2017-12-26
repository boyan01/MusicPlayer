package tech.summerly.quiet.commonlib.utils

import android.arch.persistence.db.SupportSQLiteDatabase

/**
 * Created by summer on 17-12-26
 */
fun SupportSQLiteDatabase.inTransaction(block: SupportSQLiteDatabase.() -> Unit) {
    beginTransaction()
    try {
        block()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}