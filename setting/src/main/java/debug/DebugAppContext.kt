package debug

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule

/**
 * Created by summer on 18-2-12
 */
internal class DebugAppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseModule.init(this)
    }
}