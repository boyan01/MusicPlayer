package tech.soit.quiet.ui.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.app.NotificationCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.utils.Dummy
import tech.soit.quiet.utils.mock
import tech.soit.quiet.utils.test.getPropertyValue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author : summer
 * @date : 18-8-28
 */
@RunWith(AndroidJUnit4::class)
class MusicNotificationTest {

    @get:Rule
    val task = InstantTaskExecutorRule()

    private lateinit var onCancel: () -> Unit

    private lateinit var onNotify: (builder: NotificationCompat.Builder, cancelAble: Boolean) -> Unit

    private lateinit var helper: MusicNotification


    @Before
    fun setUp() {
        onCancel = mock()
        Mockito.`when`(onCancel.invoke()).thenReturn(Unit)
        onNotify = mock()
        Mockito.`when`(onNotify.invoke(Mockito.any(), Mockito.anyBoolean())).thenReturn(Unit)
        helper = MusicNotification()

    }

    @After
    fun tearDown() {
        MusicPlayerManager.playerState.postValue(IMediaPlayer.IDLE)
        MusicPlayerManager.playingMusic.postValue(null)
    }

    @Test
    fun testBasicWithoutPic() {
        MusicPlayerManager.playingMusic.postValue(Dummy.MUSICS[0])
        MusicPlayerManager.playerState.postValue(IMediaPlayer.PLAYING)

        CountDownLatch(1).await(2000, TimeUnit.MILLISECONDS)

        helper.checkNotification(onNotify, onCancel)

        Mockito.verify(onNotify(Mockito.any(), false), Mockito.times(1))
    }


    @Test
    fun testBasicWithPic() {
        val music = Dummy.MUSICS[0]

        MusicPlayerManager.playingMusic.postValue(music)
        MusicPlayerManager.playerState.postValue(IMediaPlayer.PLAYING)

        helper.checkNotification(onNotify, onCancel)

        CountDownLatch(1).await(2000, TimeUnit.MILLISECONDS)

        if (helper.getPropertyValue("isNotifyCompleted")) {
            Mockito.verify(onNotify(Mockito.any(), false), Mockito.times(2))

        } else {
            //will not be 2 when device access PIC_URL failed
            Mockito.verify(onNotify(Mockito.any(), false), Mockito.times(1))
        }

    }


}