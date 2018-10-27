package tech.soit.quiet.ui.fragment.local

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.R
import tech.soit.quiet.model.local.LocalAlbum
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.ViewModelUtil
import tech.soit.quiet.utils.testing.SingleFragmentActivity
import tech.soit.quiet.viewmodel.LocalMusicViewModel

/**
 * @author : summer
 * @date : 18-9-2
 */
@RunWith(AndroidJUnit4::class)
class LocalAlbumFragmentTest {

    @get:Rule
    val activity = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    private val localAlbumFragment = LocalAlbumFragment()
    private lateinit var viewModel: LocalMusicViewModel

    private val albums = MutableLiveData<List<Album>>()

    @Before
    fun setUp() {
        viewModel = mock()
        Mockito.`when`(viewModel.allAlbums).thenReturn(albums)
        localAlbumFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        activity.activity.setFragment(localAlbumFragment)
    }

    @Test
    fun testBasic() {

        albums.postValue(null)
        Espresso.onView(ViewMatchers.withId(R.id.itemLoadingLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        albums.postValue(emptyList())
        Espresso.onView(ViewMatchers.withId(R.id.itemEmptyLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val albumsData = (1..10).map {
            LocalAlbum("test album $it", "...")
        }
        albums.postValue(albumsData)
        Espresso.onView(withId(R.id.recyclerView)).check { view, noViewFoundException ->
            if (view is RecyclerView) {
                assertThat(view.adapter!!.itemCount, `is`(albumsData.size))
            } else {
                throw noViewFoundException
            }
        }

    }

}