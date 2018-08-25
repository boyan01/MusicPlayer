package tech.soit.quiet.player.core

import android.media.MediaPlayer
import android.net.Uri
import androidx.test.InstrumentationRegistry
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class QuietMediaPlayerTest {

    companion object {


        val URI = Uri.parse("android.resource://"
                + InstrumentationRegistry.getContext().packageName + "/raw/summer").toString()


    }

    private lateinit var androidMediaPlayer: MediaPlayer

    private lateinit var quietMediaPlayer: QuietMediaPlayer

    @Before
    fun setUp() {
        androidMediaPlayer = MediaPlayer()
        quietMediaPlayer = QuietMediaPlayer(androidMediaPlayer)
    }

    @After
    fun close() {
        quietMediaPlayer.release()
    }

    @Test
    fun basic() = runBlocking {
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.IDLE)
        quietMediaPlayer.prepare(URI, true)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PREPARING)
        delay(1000)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PLAYING)
    }

    @Test
    fun testPlayAndPause() = runBlocking {
        quietMediaPlayer.prepare(URI, false)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PREPARING)

        delay(1000)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PAUSING)

        quietMediaPlayer.isPlayWhenReady = true
        delay(1000)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PLAYING)

        quietMediaPlayer.isPlayWhenReady = false
        delay(1000)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PAUSING)

    }

    @Test
    fun seekTo() = runBlocking {
        quietMediaPlayer.prepare(URI, true)
        delay(1000)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PLAYING)

        assertTrue("current position(${quietMediaPlayer.getPosition()}) less than 2000", quietMediaPlayer.getPosition() < 2000)

        quietMediaPlayer.seekTo(4000)
        delay(1000)
        assertTrue("current position(${quietMediaPlayer.getPosition()} greater than 3500", quietMediaPlayer.getPosition() > 3500)

    }


    @Test
    fun getDuration() = runBlocking {
        assertEquals(quietMediaPlayer.getDuration(), 0L)
        quietMediaPlayer.prepare(URI, false)
        delay(1000)
        assertTrue(quietMediaPlayer.getState() == IMediaPlayer.PAUSING)

        assertTrue("duration ${quietMediaPlayer.getDuration()} greater than 7000", quietMediaPlayer.getDuration() > 7000)
        assertTrue("duration ${quietMediaPlayer.getDuration()} less than 9000", quietMediaPlayer.getDuration() < 9000)
    }

    @Test
    fun testPlayOther() = runBlocking {

        quietMediaPlayer.prepare(URI, true)
        delay(1000)
        assertEquals("player is playing", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

        quietMediaPlayer.prepare(URI, true)
        delay(1000)
        assertEquals("play an other music is still playing !!", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

    }

    @Test
    fun testRelease() = runBlocking {

        quietMediaPlayer.prepare(URI, true)
        delay(1000)
        assertEquals("player is playing..", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

        quietMediaPlayer.release()

        assertEquals("player has been release", IMediaPlayer.IDLE, quietMediaPlayer.getState())
        assertFalse("player has been release", quietMediaPlayer.isPlayWhenReady)

        quietMediaPlayer.prepare(URI, true)
        delay(1000)
        assertEquals("player replay after release", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

    }
}