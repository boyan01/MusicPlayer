package tech.soit.quiet.ui.activity.user

import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import tech.soit.quiet.R
import tech.soit.quiet.model.po.NeteaseUser
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.ui.activity.user.viewmodel.LoginViewModel
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.BaseActivityTestRule
import tech.soit.quiet.utils.test.ViewModelUtil

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val activityRule = BaseActivityTestRule(LoginActivity::class) {
        val repository = mock<NeteaseRepository>()

        runBlocking {
            `when`(repository.login(Mockito.anyString(), Mockito.anyString())).thenReturn(NeteaseUser(0, "test", ""))
        }

        viewModel = LoginViewModel(repository)
        viewModelFactory = ViewModelUtil.createFor(viewModel)
    }

    @Test
    fun testLogin() {

        val am = InstrumentationRegistry
                .getInstrumentation()
                .addMonitor("tech.soit.quiet.ui.activity.main.AppMainActivity", null, false)

        Instrumentation().addMonitor(am)

        onView(withId(R.id.editPhone)).perform(replaceText("12345678910"))
        onView(withId(R.id.editPassword)).perform(replaceText("password"))

        onView(withId(R.id.buttonLogin)).perform(click())

        am.waitForActivityWithTimeout(1000)
        Assert.assertEquals("main activity has been launched!!", 1, am.hits)

    }

}