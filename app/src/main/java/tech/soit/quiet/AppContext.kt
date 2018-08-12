package tech.soit.quiet

import android.app.Application

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
    }
}