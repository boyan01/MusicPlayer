package tech.soit.quiet.repository.db.dao

import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.repository.db.QuietDatabaseTest
import tech.soit.quiet.repository.db.po.LocalMusic

@RunWith(AndroidJUnit4::class)
class LocalMusicDaoTest {


    companion object {


        private val DUMMY_MUSICS = listOf(
                LocalMusic(0, "file://test/01", "music01", "album01", "artist01"),
                LocalMusic(0, "file://test/02", "music02", "album02", "artist0102"),
                LocalMusic(0, "file://test/03", "music03", "album01", "artist01"),
                LocalMusic(0, "file://test/04", "music04", "album02", "artist0302"),
                LocalMusic(0, "file://test/05", "music05", "album01", "artist01")
        )

    }


    private lateinit var db: QuietDatabase


    @Before
    fun initDb() {
        db = QuietDatabaseTest.instance
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testEmptyQuery() {
        val musics = db.localMusicDao().getAllMusics()
        Assert.assertTrue(musics.isEmpty())
    }

    @Test
    fun insertMusic() {
        val ids = DUMMY_MUSICS.map {
            db.localMusicDao().insertMusic(it)
        }
        val musics = db.localMusicDao().getAllMusics()
        ids.forEach { id ->
            Assert.assertTrue(musics.find { it.id == id } != null)
        }
    }


}