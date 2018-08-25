package tech.soit.quiet.ui.fragment.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.repository.db.QuietDatabaseTest
import tech.soit.quiet.repository.db.QuietDatabaseTest.Companion.DUMMY_MUSICS
import tech.soit.quiet.repository.db.await
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.utils.component.support.liveDataWith
import tech.soit.quiet.utils.mock
import tech.soit.quiet.viewmodel.LocalMusicViewModel

@RunWith(AndroidJUnit4::class)
class LocalMusicViewModelTest {

    private lateinit var viewModel: LocalMusicViewModel

    private val localMusicDao = mock<LocalMusicDao>()

    @get:Rule
    val r = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = LocalMusicViewModel(localMusicDao)
        Mockito.`when`(localMusicDao.getAllMusics())
                .thenReturn(liveDataWith(QuietDatabaseTest.DUMMY_MUSICS))
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