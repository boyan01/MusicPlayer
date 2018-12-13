package tech.soit.quiet.ui.fragment.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.FakeUser
import tech.soit.quiet.model.po.NeteaseUser
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.ui.activity.user.LoginActivity
import tech.soit.quiet.ui.fragment.home.viewmodel.MainMusicViewModel
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.utils.testing.SingleFragmentActivity
import kotlin.reflect.jvm.jvmName

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

        onView(withId(R.id.textUserNickname)).check(matches(withText(R.string.user_not_login)))
        val am = InstrumentationRegistry.getInstrumentation().addMonitor(LoginActivity::class.jvmName, null, false)

        onView(withId(R.id.textUserNickname)).perform(click())

        am.waitForActivity()
        Assert.assertEquals("login activity has been opened", 1, am.hits)
    }

    @Test
    fun testWithLogin() {
        Mockito.`when`(neteaseRepository.getLoginUser()).thenReturn(NeteaseUser(100, "quiet", "null"))
        activityTestRule.activity.setFragment(mainMusicFragment)

        onView(withId(R.id.tabLayoutPlayLists)).check(matches(isDisplayed()))
        onView(withId(R.id.textUserNickname)).check(matches(withText("quiet")))
    }


    class FakePlaylist(private val position: Int) : PlayListDetail() {
        override fun getCoverUrl(): Any {
            return R.color.color_primary
        }

        override fun getCreator(): User {
            return FakeUser(0)
        }

        override fun getTracks(): List<Music> {
            return emptyList()
        }

        override fun isSubscribed(): Boolean {
            return true
        }

        override fun getPlayCount(): Int {
            return 0
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


    }

}