package debug

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule

/**
 * Created by summer on 18-3-17
 */

class DebugAppContext : Application() {


    override fun onCreate() {
        super.onCreate()
        BaseModule(this, BaseModule.CLASS_NAME_LIB)
    }

}