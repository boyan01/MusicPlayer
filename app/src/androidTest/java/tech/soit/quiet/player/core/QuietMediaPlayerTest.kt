package tech.soit.quiet.player.core

import android.media.MediaPlayer
import android.net.Uri
import androidx.test.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import tech.soit.quiet.utils.mock

class QuietMediaPlayerTest {

    companion object {

        val URI = Uri.parse("android.resource://"
                + InstrumentationRegistry.getContext().packageName + "/raw/summer").toString()


    }

    private lateinit var androidMediaPlayer: MediaPlayer

    private lateinit var quietMediaPlayer: QuietMediaPlayer

    @Before
    fun setUp() {
        androidMediaPlayer = mock()
        quietMediaPlayer = QuietMediaPlayer(androidMediaPlayer)

        //mock MediaPlayer.setOnPreparedListener方法
        var listener: MediaPlayer.OnPreparedListener? = null
        Mockito.doAnswer {
            listener = it.getArgument(0) as MediaPlayer.OnPreparedListener
            Unit
        }.`when`(androidMediaPlayer).setOnPreparedListener(Mockito.any())
        Mockito.`when`(androidMediaPlayer.prepareAsync()).then {
            //当prepareAsync被调用时，立即调用回调
            assertEquals(IMediaPlayer.PREPARING, quietMediaPlayer.getState())
            listener!!.onPrepared(androidMediaPlayer)
        }

    }

    @After
    fun close() {
        quietMediaPlayer.release()
    }

    /**
     * 测试 [QuietMediaPlayer.prepare] 方法
     */
    @Test
    fun testPrepare() {

        assertEquals(IMediaPlayer.IDLE, quietMediaPlayer.getState())

        quietMediaPlayer.prepare(URI, true)

        assertEquals(IMediaPlayer.PLAYING, quietMediaPlayer.getState())
    }

    @Test
    fun testPlayAndPause() {
        quietMediaPlayer.prepare(URI, false)
        assertEquals(IMediaPlayer.PAUSING, quietMediaPlayer.getState())

        quietMediaPlayer.isPlayWhenReady = true
        assertEquals(IMediaPlayer.PLAYING, quietMediaPlayer.getState())

        quietMediaPlayer.isPlayWhenReady = false
        assertEquals(IMediaPlayer.PAUSING, quietMediaPlayer.getState())
    }

    @Test
    fun seekTo() {
        quietMediaPlayer.prepare(URI, true)

        var position = 0

        Mockito.`when`(androidMediaPlayer.seekTo(Mockito.anyInt())).then {
            position = it.getArgument(0) as Int
            Unit
        }

        Mockito.`when`(quietMediaPlayer.getPosition()).then { position }

        quietMediaPlayer.seekTo(1000)
        assertEquals(1000, quietMediaPlayer.getPosition())

        quietMediaPlayer.seekTo(4000)
        assertEquals(4000, quietMediaPlayer.getPosition())
    }


    @Test
    fun getDuration() {
        Mockito.`when`(androidMediaPlayer.duration).then { 8000 }

        //it will be zero , because do not start play
        assertEquals(0, quietMediaPlayer.getDuration())

        quietMediaPlayer.prepare(URI, false)

        assertEquals(8000, quietMediaPlayer.getDuration())
    }

    @Test
    fun testPlayOther() {

        quietMediaPlayer.prepare(URI, true)
        assertEquals("player is playing", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

        quietMediaPlayer.prepare(URI, true)
        assertEquals("play an other music is still playing !!", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

    }

    @Test
    fun testRelease() {

        quietMediaPlayer.prepare(URI, true)

        quietMediaPlayer.release()

        assertEquals("player has been release", IMediaPlayer.IDLE, quietMediaPlayer.getState())
        assertFalse("player has been release", quietMediaPlayer.isPlayWhenReady)

        quietMediaPlayer.prepare(URI, true)
        assertEquals("player replay after release", IMediaPlayer.PLAYING, quietMediaPlayer.getState())

    }
}