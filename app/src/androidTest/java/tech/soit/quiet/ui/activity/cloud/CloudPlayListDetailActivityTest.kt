package tech.soit.quiet.ui.activity.cloud

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.FakePlayListDetail
import tech.soit.quiet.model.FakeUser
import tech.soit.quiet.ui.activity.cloud.CloudPlayListDetailActivity.Companion.PARAM_ID
import tech.soit.quiet.ui.activity.cloud.viewmodel.CloudPlayListDetailViewModel
import tech.soit.quiet.utils.Dummy
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.BaseActivityTestRule
import tech.soit.quiet.utils.test.ViewModelUtil

@RunWith(AndroidJUnit4::class)
class CloudPlayListDetailActivityTest {


    private lateinit var viewModel: CloudPlayListDetailViewModel


    @get:Rule
    val activityRule = BaseActivityTestRule(CloudPlayListDetailActivity::class, false)

    @Test
    fun testSubscribeText() {
        viewModel = mock()
        BaseActivityTestRule.viewModelFactory = ViewModelUtil.createFor(viewModel)

        val login = FakeUser(1, "login", "")
        runBlocking {
            Mockito.`when`(viewModel.loadData(1000L)).thenReturn(FakePlayListDetail(
                    1000L, "test", "", login, Dummy.MUSICS, false, 1000
            ))
            Mockito.`when`(viewModel.getLoginUser()).thenReturn(login)
        }
        val intent = activityRule.activityIntent
        intent.putExtra(PARAM_ID, 1000L)
        activityRule.launchActivity(intent)

        onView(withId(R.id.textCollection)).check(matches(not(isDisplayed())))
        activityRule.finishActivity()

    }


}