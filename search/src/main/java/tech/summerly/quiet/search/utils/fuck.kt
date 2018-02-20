package tech.summerly.quiet.search.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi


fun FragmentManager.inTransaction(transaction: FragmentTransaction.() -> Unit) {
    val t = beginTransaction()
    try {
        t.transaction()
    } finally {
        t.commit()
    }
}


val neteaseMusicService: NeteaseCloudMusicApi
    get() = NeteaseCloudMusicApi()


