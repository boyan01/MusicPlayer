package tech.summerly.quiet

import android.app.Application

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
    }
}