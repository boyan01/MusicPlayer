package tech.soit.quiet.player.playlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.soit.quiet.player.PlayMode
import tech.soit.quiet.utils.Dummy

@RunWith(AndroidJUnit4::class)
class PlaylistTest {


    private lateinit var playlist: Playlist

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

        val insert = Dummy.DummyMusic(10000, "inserted", "14", emptyList())
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