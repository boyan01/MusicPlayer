package tech.summerly.quiet.commonlib.player.playlist

import android.support.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayMode
import tech.summerly.quiet.commonlib.player.PlayerType

/**
 * test for [NormalPlaylist2]
 */
@RunWith(AndroidJUnit4::class)
open class NormalPlaylist2Test {

    protected class TestObject(val id: Long)

    protected fun generateTestObjects(): List<TestObject> {
        return (0..10).map {
            TestObject(it.toLong())
        }
    }

    companion object {
        const val TOKEN = "test"
    }

    private val playlist = NormalPlaylist2<TestObject>(TOKEN, ArrayList())


    @Test
    fun checkToken() {
        assertEquals(playlist.token, TOKEN)
    }


    @Test
    fun checkType() {
        assertEquals(playlist.type, PlayerType.NORMAL)
    }


    @Test
    fun sequenceMode() = runBlocking {
        val testObjects = generateTestObjects()
        playlist.reset(testObjects)

        MusicPlayerManager.player.playMode = PlayMode.Sequence
        assertTrue(playlist.getNext() == testObjects[0])
        for (i in 0 until 10) {
            assertTrue(playlist.getNext(testObjects[i]) == testObjects[i + 1])
        }
        assertTrue(playlist.getPrevious() == testObjects[0])
        for (i in 1..10) {
            assertTrue(playlist.getPrevious(testObjects[i]) == testObjects[i - 1])
        }

        Unit
    }

}