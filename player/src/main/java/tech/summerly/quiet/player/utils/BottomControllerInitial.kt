package tech.summerly.quiet.player.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import tech.summerly.quiet.commonlib.component.callback.BottomControllerHost

/**
 * util class to help activity which host bottom controller to init the bottom controller
 */
internal object BottomControllerInitial : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
        if (activity is BottomControllerHost) {
            activity.initBottomController()
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

}