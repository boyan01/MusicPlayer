package debug

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule

/**
 * Created by summer on 18-2-17
 */
class DebugAppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseModule(this, "tech.summerly.quiet.commonlib.LibModule")
        BaseModule(this, "tech.summerly.quiet.search.SearchModule")
    }
}