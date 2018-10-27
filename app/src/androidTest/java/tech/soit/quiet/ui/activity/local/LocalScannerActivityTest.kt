package tech.soit.quiet.ui.activity.local

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.utils.Dummy
import tech.soit.quiet.utils.component.support.Resource
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.BaseActivityTestRule
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.viewmodel.LocalScannerViewModel

@RunWith(AndroidJUnit4::class)
class LocalScannerActivityTest {


    private lateinit var viewModel: LocalScannerViewModel

    private val newAdded = MutableLiveData<Resource<Music>>()
    private val status = MutableLiveData<Int>()

    @get:Rule
    val activityRule = BaseActivityTestRule(LocalScannerActivity::class) {
        viewModel = mock()
        Mockito.`when`(viewModel.newAdded).thenReturn(newAdded)
        Mockito.`when`(viewModel.status).thenReturn(status)
        viewModelFactory = ViewModelUtil.createFor(viewModel)
    }

    @get:Rule
    val readStorage = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE)!!

    @Test
    fun testFirstIn() {
        status.postValue(LocalScannerViewModel.STATUS_IDLE)

        Espresso.onView(ViewMatchers.withId(R.id.scanningLayout)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.resultLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.textResult)).check(ViewAssertions.matches(ViewMatchers.withText(R.string.local_scanner_hint)))
    }

    @Test
    fun textScanning() {

        Espresso.onView(ViewMatchers.withId(R.id.buttonStart)).perform(ViewActions.click())
        Mockito.verify(viewModel).startScan()

        status.postValue(LocalScannerViewModel.STATUS_SCANNING)
        newAdded.postValue(Resource.success(Dummy.MUSICS[0]))

        Espresso.onView(ViewMatchers.withId(R.id.scanningLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.textLoading)).check(ViewAssertions.matches(ViewMatchers.withText(string(R.string.processing_local_scanner, Dummy.MUSICS[0].getTitle()))))

    }

    @Test
    fun testStopScan() {
        status.postValue(LocalScannerViewModel.STATUS_SCANNING)
        Espresso.onView(ViewMatchers.withId(R.id.buttonEnd)).perform(ViewActions.click())
        Mockito.verify(viewModel).stopScan()
    }

    @Test
    fun testEnd() {
        val activity = activityRule.activity

        Mockito.`when`(viewModel.resultCount).thenReturn(5)
        status.postValue(LocalScannerViewModel.STATUS_SUCCESS)

        Espresso.onView(ViewMatchers.withId(R.id.scanningLayout)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.textResult)).check(ViewAssertions.matches(ViewMatchers.withText(string(R.string.template_scanner_result, 5))))

        Espresso.onView(ViewMatchers.withId(R.id.buttonEnd)).perform(ViewActions.click())

        Assert.assertTrue("activity is destroyed", activity == null || activity.isDestroyed || activity.isFinishing)
    }


}