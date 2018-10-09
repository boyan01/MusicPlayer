package tech.soit.quiet.utils.test

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.test.rule.ActivityTestRule
import tech.soit.quiet.ui.activity.base.BaseActivity
import kotlin.reflect.KClass

class BaseActivityTestRule<T : BaseActivity>(
        activityClass: KClass<T>,
        launchActivity: Boolean = true,
        private val beforeActivityLaunched: (BaseActivityTestRule.Companion.() -> Unit)? = null
) : ActivityTestRule<T>(activityClass.java, true, launchActivity) {

    companion object {

        var viewModelFactory: ViewModelProvider.Factory? = null

        /**
         * this function is called by [BaseActivity.onCreate]
         */
        fun injectActivity(activity: BaseActivity) {
            //check again
            val isTest = activity.intent.getBooleanExtra("isTest", false)
            if (!isTest) {
                return
            }
            viewModelFactory?.let {
                activity.viewModelFactory = it
                viewModelFactory = null
            }
        }


    }


    override fun beforeActivityLaunched() {
        beforeActivityLaunched?.invoke(Companion)
    }


    /**
     * add a default identify value
     */
    override fun getActivityIntent(): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.putExtra("isTest", true)
        return intent
    }


}