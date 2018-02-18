package tech.summerly.quiet.search.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.service.NeteaseMusicService


fun FragmentManager.inTransaction(transaction: FragmentTransaction.() -> Unit) {
    val t = beginTransaction()
    try {
        t.transaction()
    } finally {
        t.commit()
    }
}


val neteaseMusicService: NeteaseMusicService?
    get() = ARouter.getInstance().build("/netease/api").navigation() as NeteaseMusicService?


