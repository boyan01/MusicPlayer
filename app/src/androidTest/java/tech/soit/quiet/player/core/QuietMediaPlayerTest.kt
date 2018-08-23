package tech.soit.quiet.player.core

import android.media.MediaPlayer
import android.net.Uri
import androidx.test.InstrumentationRegistry
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.IDLE)
        quietMediaPlayer.prepare(URI, true)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PREPARING)
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PLAYING)
    }

    @Test
    fun testPlayAndPause() = runBlocking {
        quietMediaPlayer.prepare(URI, false)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PREPARING)

        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PAUSING)

        quietMediaPlayer.isPlayWhenReady = true
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PLAYING)

        quietMediaPlayer.isPlayWhenReady = false
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PAUSING)

    }

    @Test
    fun seekTo() = runBlocking {
        quietMediaPlayer.prepare(URI, true)
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PLAYING)

        assertTrue(quietMediaPlayer.getPosition() < 1000)

        quietMediaPlayer.seekTo(3000)

        assertTrue(quietMediaPlayer.getPosition() > 2500)

    }


    @Test
    fun getDuration() = runBlocking {
        assertEquals(quietMediaPlayer.getDuration(), 0L)
        quietMediaPlayer.prepare(URI, false)
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PAUSING)

        assertTrue(quietMediaPlayer.getDuration() > 5000)
    }

    @Test
    fun testPlayOther() = runBlocking {

        quietMediaPlayer.prepare(URI, true)

        delay(20)

        quietMediaPlayer.prepare(URI, true)
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PLAYING)

    }

    @Test
    fun testRelease() = runBlocking {

        quietMediaPlayer.prepare(URI, true)
        delay(20)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PLAYING)

        quietMediaPlayer.release()
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.IDLE)

        quietMediaPlayer.prepare(URI, true)
        delay(100)
        assertTrue(quietMediaPlayer.getState().value == IMediaPlayer.PLAYING)

    }
}