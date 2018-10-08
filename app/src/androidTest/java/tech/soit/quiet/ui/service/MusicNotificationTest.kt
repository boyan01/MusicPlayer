package tech.soit.quiet.ui.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.app.NotificationCompat
import androidx.test.runner.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.*
import org.junit.runner.RunWith
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.utils.Dummy
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
        onCancel = mockk()
        every { onCancel() }.returns(Unit)
        onNotify = mockk()
        every { onNotify(allAny(), allAny()) }.returns(Unit)

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

        verify(exactly = 1) { onNotify(allAny(), false) }
    }


    @Test
    fun testBasicWithPic() {
        val music = Dummy.MUSICS[0].copy(attach = mapOf(Music.PIC_URI to "https://via.placeholder.com/350x150"))

        MusicPlayerManager.playingMusic.postValue(music)
        MusicPlayerManager.playerState.postValue(IMediaPlayer.PLAYING)

        helper.checkNotification(onNotify, onCancel)

        CountDownLatch(1).await(2000, TimeUnit.MILLISECONDS)

        if (helper.getPropertyValue("isNotifyCompleted")) {
            verify(exactly = 2) { onNotify(allAny(), false) }
        } else {
            //will not be 2 when device access PIC_URL failed
            verify(exactly = 1) { onNotify(allAny(), false) }
        }

    }


}