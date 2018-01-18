package tech.summerly.quiet

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
class AppContext : Application() {

    private val modules = ArrayList<BaseModule>()

    override fun onCreate() {
        super.onCreate()
        modules.add(BaseModule(this, "tech.summerly.quiet.commonlib.LibModule"))
        modules.add(BaseModule(this, "tech.summerly.quiet.netease.NeteaseModule"))
        modules.add(BaseModule(this, "tech.summerly.quiet.local.LocalModule"))
    }
}