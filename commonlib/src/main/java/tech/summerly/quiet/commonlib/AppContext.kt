package tech.summerly.quiet.commonlib

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.stetho.Stetho
import tech.summerly.quiet.commonlib.player.MusicPlayerManager

/**
 * Created by summer on 17-12-17.
 * Base Application context
 */
class AppContext : Application() {

    companion object {
        private var context: AppContext? = null

        val instance: AppContext
            get() = context!!
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        MusicPlayerManager.init(this)
        Stetho.initializeWithDefaults(this)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }
}