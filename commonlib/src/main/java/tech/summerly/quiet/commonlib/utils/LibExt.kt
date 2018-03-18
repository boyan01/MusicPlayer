package tech.summerly.quiet.commonlib.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

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