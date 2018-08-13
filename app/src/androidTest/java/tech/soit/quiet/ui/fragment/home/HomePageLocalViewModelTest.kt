package tech.soit.quiet.ui.fragment.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.repository.db.QuietDatabaseTest
import tech.soit.quiet.repository.db.await

import tech.soit.quiet.repository.db.QuietDatabaseTest.Companion.DUMMY_MUSICS

@RunWith(AndroidJUnit4::class)
class HomePageLocalViewModelTest {

    private lateinit var db: QuietDatabase

    private lateinit var viewModel: HomePageLocalViewModel

    @get:Rule
    val r = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        db = QuietDatabaseTest.instance
        viewModel = HomePageLocalViewModel(db)
        DUMMY_MUSICS.forEach {
            db.localMusicDao().insertMusic(it)
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAllMusics() {
        val list = viewModel.allMusics.await()
        assertTrue(list.size == DUMMY_MUSICS.size)
        DUMMY_MUSICS.forEach { localMusic ->
            assertNotNull(list.find { it.title == localMusic.title })
        }
    }

    @Test
    fun getAllAlbums() {
        val list = viewModel.allAlbums.await()
        assertTrue(list.size == 2)
        assertNotNull(list.find { it.title == "album01" })
        assertNotNull(list.find { it.title == "album02" })
    }

    @Test
    fun getAllArtists() {
        val list = viewModel.allArtists.await()
        assertTrue(list.size == 3)
        assertNotNull(list.find { it.name == "artist01" })
        assertNotNull(list.find { it.name == "artist0102" })
        assertNotNull(list.find { it.name == "artist0302" })
    }
}