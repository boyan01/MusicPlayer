package tech.summerly.quiet

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule

/**
 * application context
 */
class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseModule.init(this)
    }
}