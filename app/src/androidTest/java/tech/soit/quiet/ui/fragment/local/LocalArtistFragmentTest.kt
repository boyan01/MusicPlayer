package tech.soit.quiet.ui.fragment.local

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.utils.testing.SingleFragmentActivity
import tech.soit.quiet.viewmodel.LocalMusicViewModel

/**
 * @author : summer
 * @date : 18-9-2
 */
@RunWith(AndroidJUnit4::class)
class LocalArtistFragmentTest {

    @get:Rule
    val activity = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val fragment = LocalArtistFragment()

    private val liveData = MutableLiveData<List<Artist>>()

    private lateinit var viewModel: LocalMusicViewModel

    @Before
    fun setUp() {
        viewModel = mock()
        Mockito.`when`(viewModel.allArtists).thenReturn(liveData)
        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel)

        activity.activity.setFragment(fragment)
    }

    @Test
    fun testBack() {
        liveData.postValue(null)
        Espresso.onView(ViewMatchers.withId(R.id.itemLoadingLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        liveData.postValue(emptyList())
        Espresso.onView(ViewMatchers.withId(R.id.itemEmptyLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val artists = (0..10).map {
            Artist("artist $it")
        }

        liveData.postValue(artists)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).check { view, noViewFoundException ->
            if (view is RecyclerView) {
                ViewMatchers.assertThat(view.adapter!!.itemCount, CoreMatchers.`is`(artists.size))
            } else {
                throw noViewFoundException
            }
        }
    }
}