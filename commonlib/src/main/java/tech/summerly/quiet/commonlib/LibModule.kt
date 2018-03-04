package tech.summerly.quiet.commonlib

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.stetho.Stetho
import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.streamcache.StreamCacheUtil

/**
 * Created by summer on 17-12-17.
 * Base Application context
 */
internal object LibModule : BaseModule() {


    override fun onCreate() {

        Stetho.initializeWithDefaults(this)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(applicationContext as Application?)

        StreamCacheUtil.init(this)
    }

}