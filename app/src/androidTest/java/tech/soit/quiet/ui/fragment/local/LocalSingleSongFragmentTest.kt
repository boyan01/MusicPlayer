package tech.soit.quiet.ui.fragment.local

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.utils.Dummy
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.RecyclerViewMatcher
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.utils.testing.SingleFragmentActivity
import tech.soit.quiet.viewmodel.LocalMusicViewModel

@RunWith(AndroidJUnit4::class)
class LocalSingleSongFragmentTest {

    @get:Rule
    val activity = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val localSingleSongFragment = LocalSingleSongFragment()
    private lateinit var viewModel: LocalMusicViewModel

    private val musicList = MutableLiveData<List<Music>>()

    @Before
    fun setUp() {
        viewModel = mock()
        Mockito.`when`(viewModel.allMusics).thenReturn(musicList)
        localSingleSongFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        activity.activity.setFragment(localSingleSongFragment)
    }


    @Test
    fun testLoading() {
        musicList.postValue(null)
        onView(withId(R.id.itemLoadingLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(1)))
    }

    @Test
    fun testEmpty() {
        musicList.postValue(emptyList())
        onView(withId(R.id.itemEmptyLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(hasChildCount(1)))
    }

    @Test
    fun testShowMusicList() {
        val musics = Dummy.MUSICS
        musicList.postValue(musics)

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText(musics[0].title))))
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recyclerView)
    }

}