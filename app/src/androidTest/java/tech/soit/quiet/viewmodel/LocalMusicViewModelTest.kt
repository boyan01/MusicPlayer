package tech.soit.quiet.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
            assertNotNull(list.find { it.getTitle() == localMusic.getTitle() })
        }
    }

    @Test
    fun getAllAlbums() {
        val list = viewModel.allAlbums.await()
        assertTrue(list.size == 2)
        assertNotNull(list.find { it.getName() == "album01" })
        assertNotNull(list.find { it.getName() == "album02" })
    }

    @Test
    fun getAllArtists() {
        val list = viewModel.allArtists.await()
        assertTrue(list.size == 3)
        assertNotNull(list.find { it.getName() == "artist01" })
        assertNotNull(list.find { it.getName() == "artist0102" })
        assertNotNull(list.find { it.getName() == "artist0302" })
    }
}