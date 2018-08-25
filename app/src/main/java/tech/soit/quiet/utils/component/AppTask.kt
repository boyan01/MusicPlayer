package tech.soit.quiet.utils.component

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.collection.ArraySet
import tech.soit.quiet.ui.activity.base.BaseActivity

object AppTask {


    //处于可见状态中的 activities
    private val activeActivities = ArraySet<BaseActivity>()

    //可用的 Activities
    private val availableActivities = ArraySet<BaseActivity>()

    var topStackActivity: BaseActivity? = null
        private set(value) {
            field = value
        }


    fun getActiveActivities(): ArraySet<BaseActivity> {
        return ArraySet(activeActivities)
    }

    fun getAvailableActivities(): ArraySet<BaseActivity> {
        return ArraySet(availableActivities)
    }

    internal object CallBack : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            availableActivities.add(baseActivity(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activeActivities.add(baseActivity(activity))
        }


        override fun onActivityResumed(activity: Activity) {
            topStackActivity = baseActivity(activity)
            dumpStackInfo()
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity == topStackActivity) {
                topStackActivity = null
            }
        }

        override fun onActivityStopped(activity: Activity) {
            activeActivities.remove(baseActivity(activity))
        }

        override fun onActivityDestroyed(activity: Activity) {
            availableActivities.remove(baseActivity(activity))
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

        }


        private fun baseActivity(activity: Activity): BaseActivity {
            if (activity is BaseActivity) {
                return activity
            } else {
                throw IllegalStateException("all of activities must be subclass of BaseActivity")
            }
        }
    }


    fun dumpStackInfo() {
        log(LoggerLevel.DEBUG) {
            """

            top stack: $topStackActivity
            active: $activeActivities
            total: $availableActivities

        """.trimIndent()
        }
    }


}