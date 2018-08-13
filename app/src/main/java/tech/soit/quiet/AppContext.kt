package tech.soit.quiet

import android.app.Application
import tech.soit.quiet.utils.component.AppTask

/**
 * application context
 */
class AppContext : Application() {

    /**
     * singleton for application
     */
    companion object : Application()

    override fun onCreate() {
        super.onCreate()
        AppContext.attachBaseContext(this)
        registerActivityLifecycleCallbacks(AppTask.CallBack)
    }
}