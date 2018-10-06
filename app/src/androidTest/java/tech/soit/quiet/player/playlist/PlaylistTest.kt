package tech.soit.quiet.player.playlist

import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.PlayMode
import tech.soit.quiet.utils.Dummy
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class PlaylistTest {


    private lateinit var playlist: Playlist

    @get:Rule
    val r = Timeout(100, TimeUnit.MILLISECONDS)

    @Before
    fun setUp() {
        playlist = Playlist("test", Dummy.MUSICS)
    }


    @Test
    fun testGetList() {
        val p = playlist.list
        assertEquals(Dummy.MUSICS.size, p.size)
        Dummy.MUSICS.forEach {
            assertTrue(p.contains(it))
        }
    }


    @Test
    fun changeCurrent() {
        assertEquals(playlist.current, Dummy.MUSICS[0])
        playlist.current = Dummy.MUSICS[1]
        assertEquals(playlist.current, Dummy.MUSICS[1])
    }

    @Test
    fun testGetSequence() = runBlocking {
        playlist.playMode = PlayMode.Sequence
        assertEquals(playlist.current, Dummy.MUSICS[0])

        val next = playlist.getNext()
        assertEquals(next, Dummy.MUSICS[1])

        val previous = playlist.getPrevious()
        assertEquals(previous, Dummy.MUSICS[Dummy.MUSICS.size - 1])

        Unit
    }

    @Test
    fun testGetSingle() = runBlocking {
        playlist.playMode = PlayMode.Single
        assertEquals(playlist.current, Dummy.MUSICS[0])

        val next = playlist.getNext()
        assertEquals(next, Dummy.MUSICS[0])

        val previous = playlist.getPrevious()
        assertEquals(previous, Dummy.MUSICS[0])

        Unit
    }

    @Test
    fun testGetShuffle() = runBlocking {
        playlist.playMode = PlayMode.Shuffle
        assertEquals(playlist.current, Dummy.MUSICS[0])

        // can not test...
    }

    @Test
    fun testInsert() = runBlocking {

        val insert = Music(10000, "inserted", Album("14"), emptyList())
        playlist.insertToNext(insert)
        assertEquals(playlist.list.size, Dummy.MUSICS.size + 1)


        playlist.playMode = PlayMode.Sequence
        assertEquals(playlist.getNext(), insert)

        playlist.playMode = PlayMode.Single
        assertEquals(playlist.getNext(), playlist.current)

        playlist.playMode = PlayMode.Shuffle
        val inserted2 = insert.copy(id = 100001)
        playlist.insertToNext(inserted2)
        assertEquals(playlist.getNext(), inserted2)
    }

}