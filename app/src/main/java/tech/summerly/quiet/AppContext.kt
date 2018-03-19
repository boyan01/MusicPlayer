package tech.summerly.quiet

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseModule.init(this)
    }
}