package tech.soit.quiet.ui.fragment.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.po.NeteaseUser
import tech.soit.quiet.model.vo.PlayList
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.ui.fragment.home.viewmodel.MainMusicViewModel
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.utils.testing.SingleFragmentActivity

@RunWith(AndroidJUnit4::class)
class MainMusicFragmentTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val mainMusicFragment = MainMusicFragment()

    private lateinit var viewModel: MainMusicViewModel

    private val neteaseRepository: NeteaseRepository = mock()

    private val dummyPlaylist = (0..100).map { FakePlaylist(it) }

    @Before
    fun setUp() {
        runBlocking {
            Mockito.`when`(neteaseRepository.getUserPlayerList(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(dummyPlaylist)
        }
        viewModel = mock()
        Mockito.`when`(viewModel.getNeteaseRepository()).thenReturn(neteaseRepository)
        mainMusicFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
    }

    @Test
    fun testBasic() {
        activityTestRule.activity.setFragment(mainMusicFragment)
        onView(withId(R.id.navLayoutLocal)).check(matches(isDisplayed()))
        onView(withId(R.id.navLayoutHistory)).check(matches(isDisplayed()))
        onView(withId(R.id.navLayoutCollection)).check(matches(isDisplayed()))
        onView(withId(R.id.navLayoutDownload)).check(matches(isDisplayed()))
    }

    @Test
    fun testWithoutLogin() {
        Mockito.`when`(neteaseRepository.getLoginUser()).thenReturn(null)
        activityTestRule.activity.setFragment(mainMusicFragment)

    }

    @Test
    fun testWithLogin() {
        Mockito.`when`(neteaseRepository.getLoginUser()).thenReturn(NeteaseUser(100, "quiet"))
        activityTestRule.activity.setFragment(mainMusicFragment)

        onView(withId(R.id.tabLayoutPlayLists)).check(matches(isDisplayed()))
    }


    class FakePlaylist(private val position: Int) : PlayList() {
        override fun getDescription(): String {
            return "description $position"
        }

        override fun getCoverImageUrl(): Any {
            return R.color.color_primary
        }

        override fun getTrackCount(): Int {
            return position
        }

        override fun getName(): String {
            return "name $position"
        }

        override fun getId(): Long {
            return position.toLong()
        }

        override fun getUserId(): Long {
            return if (position < 50) {
                100
            } else {
                101
            }
        }

    }

}