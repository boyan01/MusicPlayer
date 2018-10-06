package tech.soit.quiet.ui.activity.base

import android.view.View
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.soit.quiet.R

@RunWith(AndroidJUnit4::class)
class BaseActivityTest {


    @get:Rule
    val activity = ActivityTestRule<MainActivity>(MainActivity::class.java, true, true)


    @Test
    fun testNormalContentExist() {

        val appContent = activity.activity.findViewById<View>(R.id.content)
        assertNotNull("View: R.id.content exist", appContent)


    }

}