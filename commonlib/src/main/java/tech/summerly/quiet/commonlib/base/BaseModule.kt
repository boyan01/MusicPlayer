package tech.summerly.quiet.commonlib.base

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import tech.summerly.quiet.commonlib.utils.log

/**
 * author : yangbin10
 * date   : 2018/1/18
 */
abstract class BaseModule : ContextWrapper(null), ComponentCallbacks2 {

    companion object {

        private const val PACKAGE_NAME = "tech.summerly.quiet"

        private const val CLASS_NAME_LIB = "$PACKAGE_NAME.commonlib.LibModule"
        private const val CLASS_NAME_LOCAL = "$PACKAGE_NAME.local.LocalModule"
        private const val CLASS_NAME_NETEASE = "$PACKAGE_NAME.netease.NeteaseModule"
        private const val CLASS_NAME_PLAYLIST_DETIAL = "$PACKAGE_NAME.playlistdetial.PDModule"
        private const val CLASS_NAME_SEARCH = "$PACKAGE_NAME.search.SearchModule"
        private const val CLASS_NAME_SETTING = "$PACKAGE_NAME.setting.SettingModule"


        private val MODULE_LIST = arrayOf(
                CLASS_NAME_LIB,
                CLASS_NAME_LOCAL,
                CLASS_NAME_NETEASE,
                CLASS_NAME_PLAYLIST_DETIAL,
                CLASS_NAME_SEARCH,
                CLASS_NAME_SETTING
        )

        /**
         * initial a module for class name
         * module class music be Kotlin object class
         */
        operator fun invoke(application: Application, className: String): BaseModule? {
            //field instance is already initial
            val moduleClass = try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                return null
            }
            val instance = moduleClass.getDeclaredField("INSTANCE").get(null)
            val method = moduleClass.superclass.getDeclaredMethod("attachBaseContext", Context::class.java)
            method.isAccessible = true
            method.invoke(instance, application)
            return instance as BaseModule
        }


        @JvmStatic
        fun init(application: Application) {
            MODULE_LIST.forEach {
                val module = invoke(application, it)
                log { "初始化 $it : ${if (module != null) "成功" else "失败"}" }
            }
        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        base.registerComponentCallbacks(this)
        onCreate()
    }

    open fun onCreate() {

    }

    override fun onLowMemory() {

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
    }

    override fun onTrimMemory(level: Int) {

    }

}