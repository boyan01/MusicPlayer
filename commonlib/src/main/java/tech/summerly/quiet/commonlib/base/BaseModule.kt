package tech.summerly.quiet.commonlib.base

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration

/**
 * author : yangbin10
 * date   : 2018/1/18
 */
abstract class BaseModule : ContextWrapper(null), ComponentCallbacks2 {

    companion object {

        const val CLASS_NAME_LIB = "tech.summerly.quiet.commonlib.LibModule"

        /**
         * initial a module for class name
         * module class music be Kotlin object class
         */
        operator fun invoke(application: Application, className: String): BaseModule {
            //field instance is already initial
            val moduleClass = Class.forName(className)
            val instance = moduleClass.getDeclaredField("INSTANCE").get(null)
            val method = moduleClass.superclass.getDeclaredMethod("attachBaseContext", Context::class.java)
            method.isAccessible = true
            method.invoke(instance, application)
            return instance as BaseModule
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