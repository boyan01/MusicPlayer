package tech.summerly.quiet.commonlib.component.activities

import android.app.ActivityManager
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.core.content.systemService
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log

/**
 * 当任务栈只有当前的 activity 时，按下返回键不会退出 app ，
 * 而是会打开 router 为 [parentPath] 的 activity
 */
abstract class NoIsolatedActivity : BaseActivity() {


    /**
     * router path
     */
    protected abstract val parentPath: String


    override fun onBackPressed() {
        val tasks = systemService<ActivityManager>().appTasks
        if (tasks.size != 0 && tasks[0].taskInfo.numActivities == 1) { // only current acitivty is running
            try {
                val pm = ARouter.getInstance().build(parentPath)
                LogisticsCenter.completion(pm)
                TaskStackBuilder.create(this)
                        .addNextIntent(Intent(this, pm.destination))
                        .startActivities()
            } catch (e: Exception) {
                log(LoggerLevel.ERROR) { e.printStackTrace(); " $parentPath do not match！" }
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

}