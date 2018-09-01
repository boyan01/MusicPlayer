package tech.soit.quiet.ui.fragment.local

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Before
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
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.utils.testing.SingleFragmentActivity
import tech.soit.quiet.viewmodel.LocalScannerViewModel

/**
 * @author : summer
 * @date : 18-8-30
 */
@RunWith(AndroidJUnit4::class)
class LocalMusicScannerFragmentTest {

    @get:Rule
    val activity = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val fragment = LocalMusicScannerFragment()

    private lateinit var viewModel: LocalScannerViewModel

    private val newAdded = MutableLiveData<Resource<Music>>()

    private val status = MutableLiveData<Int>()

    @Before
    fun setUp() {
        viewModel = mock()
        Mockito.`when`(viewModel.newAdded).thenReturn(newAdded)
        Mockito.`when`(viewModel.status).thenReturn(status)
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        activity.activity.setFragment(fragment)
    }


    @Test
    fun testFirstIn() {
        status.postValue(LocalScannerViewModel.STATUS_IDLE)

        onView(withId(R.id.scanningLayout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.resultLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.textResult)).check(matches(withText(R.string.local_scanner_hint)))
    }

    @Test
    fun textScanning() {

        onView(withId(R.id.buttonStart)).perform(click())
        Mockito.verify(viewModel).startScan()

        status.postValue(LocalScannerViewModel.STATUS_SCANNING)
        newAdded.postValue(Resource.success(Dummy.MUSICS[0]))

        onView(withId(R.id.scanningLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.textLoading)).check(matches(withText(string(R.string.processing_local_scanner, Dummy.MUSICS[0].title))))

    }

    @Test
    fun testStopScan() {
        status.postValue(LocalScannerViewModel.STATUS_SCANNING)
        onView(withId(R.id.buttonEnd)).perform(click())
        Mockito.verify(viewModel).stopScan()
    }

    @Test
    fun testEnd() {
        Mockito.`when`(viewModel.resultCount).thenReturn(5)
        status.postValue(LocalScannerViewModel.STATUS_SUCCESS)

        onView(withId(R.id.scanningLayout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.textResult)).check(matches(withText(string(R.string.template_scanner_result, 5))))

        onView(withId(R.id.buttonEnd)).perform(click())
        onView(withId(R.id.content)).check(matches(hasChildCount(0)))

    }

}