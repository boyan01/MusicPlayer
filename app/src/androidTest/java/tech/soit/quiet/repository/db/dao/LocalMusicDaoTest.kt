package tech.soit.quiet.repository.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.*
import org.junit.runner.RunWith
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.repository.db.QuietDatabaseTest
import tech.soit.quiet.repository.db.QuietDatabaseTest.Companion.DUMMY_MUSICS
import tech.soit.quiet.repository.db.await

@RunWith(AndroidJUnit4::class)
class LocalMusicDaoTest {


    private lateinit var db: QuietDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun initDb() {
        db = QuietDatabaseTest.instance

        //insert dummy musics first
        DUMMY_MUSICS.forEach {
            db.localMusicDao().insertMusic(it)
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testEmptyQuery() {
        db.clearAllTables()

        val musics = db.localMusicDao().getAllMusics().await()
        Assert.assertTrue(musics.isEmpty())
    }

    @Test
    fun insertMusic() {
        db.clearAllTables()

        val ids = DUMMY_MUSICS.map {
            db.localMusicDao().insertMusic(it)
        }
        val musics = db.localMusicDao().getAllMusics().await()
        ids.forEach { id ->
            Assert.assertTrue(musics.find { it.id == id } != null)
        }
    }

    @Test
    fun testFilterByArtist() {

        val list = db.localMusicDao().getMusicsByArtist("artist01").await()
        Assert.assertTrue(list.size == 3)
        Assert.assertNotNull(list.find { it.title == "music01" })
        Assert.assertNotNull(list.find { it.title == "music03" })
        Assert.assertNotNull(list.find { it.title == "music05" })
    }


    @Test
    fun testFilterByAlbum() {
        val list = db.localMusicDao().getMusicsByAlbum("album02").await()
        Assert.assertTrue(list.size == 2)
        Assert.assertNotNull(list.find { it.title == "music02" })
        Assert.assertNotNull(list.find { it.title == "music04" })
    }

}