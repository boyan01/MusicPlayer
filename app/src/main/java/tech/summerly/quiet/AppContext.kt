package tech.summerly.quiet

import android.app.Application
import android.content.Context
import tech.summerly.quiet.commonlib.LibModule

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        LibModule().onCreate(this)
        initModule("tech.summerly.quiet.netease.NeteaseModule")
        initModule("tech.summerly.quiet.local.LocalModule")
    }

    private fun initModule(className: String) {
        val neteaseModuleClass = Class.forName(className)
        val instance = neteaseModuleClass.constructors[0].newInstance()
        neteaseModuleClass.getMethod("onCreate", Context::class.java).invoke(instance, this)

    }
}