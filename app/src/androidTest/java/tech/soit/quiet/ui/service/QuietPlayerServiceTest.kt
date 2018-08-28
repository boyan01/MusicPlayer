package tech.soit.quiet.ui.service

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ServiceTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.ui.service.QuietPlayerService.Companion.action_play_next
import tech.soit.quiet.ui.service.QuietPlayerService.Companion.action_play_pause
import tech.soit.quiet.ui.service.QuietPlayerService.Companion.action_play_previous
import tech.soit.quiet.utils.Dummy
import tech.soit.quiet.utils.mock
import tech.soit.quiet.viewmodel.MusicControllerViewModel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author : summer
 * @date : 18-8-26
 */
@RunWith(AndroidJUnit4::class)
class QuietPlayerServiceTest {


    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get:Rule
    val serviceRule = ServiceTestRule()

    private fun startServiceWithAction(action: String) {
        val intent = Intent(InstrumentationRegistry.getTargetContext(), QuietPlayerService::class.java)
        intent.action = action
        InstrumentationRegistry.getTargetContext().startService(intent)
        val countDownLatch = CountDownLatch(1)
        //wait 3 seconds
        countDownLatch.await(3, TimeUnit.SECONDS)
    }

    private val musics = Dummy.MUSICS_WITH_URI

    private lateinit var notificationHelper: MusicNotification

    private lateinit var musicViewModel: MusicControllerViewModel

    private val playerState = MutableLiveData<Int>()

    private val playingMusic = MutableLiveData<Music>()

    @Before
    fun setUp() {
        notificationHelper = mock()
        musicViewModel = mock()
        QuietPlayerService.musicViewModel = musicViewModel
        QuietPlayerService.notificationHelper = notificationHelper
        Mockito.`when`(musicViewModel.playerState).thenReturn(playerState)
        Mockito.`when`(musicViewModel.playingMusic).thenReturn(playingMusic)
    }


    @Test
    fun testPlayNext() {
        startServiceWithAction(action_play_next)
        Mockito.verify(musicViewModel).playNext()
    }

    @Test
    fun testPlayPrevious() {
        startServiceWithAction(action_play_previous)
        Mockito.verify(musicViewModel).playPrevious()
    }


    @Test
    fun testPlayPause() {
        startServiceWithAction(action_play_pause)
        Mockito.verify(musicViewModel).pauseOrPlay()
    }


    @Test
    fun testLikeUnLike() {
        //TODO
    }


    @Test
    fun testNotification() {
        val iBinder = serviceRule.bindService(Intent(InstrumentationRegistry.getTargetContext(), QuietPlayerService::class.java))
        iBinder as QuietPlayerService.PlayerServiceBinder

        playingMusic.postValue(musics[0])
        playingMusic.postValue(null)
        playingMusic.postValue(musics[1])

        playerState.postValue(IMediaPlayer.PLAYING)
        playerState.postValue(IMediaPlayer.PAUSING)


        Mockito.verify(notificationHelper, Mockito.times(5)).update(iBinder.service)

    }


    @After
    fun tearDown() {
        InstrumentationRegistry.getTargetContext()
                .stopService(Intent(InstrumentationRegistry.getTargetContext(),
                        QuietPlayerService::class.java))
    }


}