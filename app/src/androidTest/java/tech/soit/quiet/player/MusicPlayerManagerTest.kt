package tech.soit.quiet.player

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.player.core.QuietMediaPlayerTest
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.repository.db.await
import tech.soit.quiet.utils.Dummy
import tech.soit.quiet.utils.component.persistence.KeyValue
import tech.soit.quiet.utils.component.persistence.get

/**
 * @author : summer
 * @date : 18-8-21
 */
@RunWith(AndroidJUnit4::class)
class MusicPlayerManagerTest {


    @get:Rule
    val l = InstantTaskExecutorRule()

    private val manager get() = MusicPlayerManager

    private val musics = Dummy.MUSICS.map {
        it.copy(attach = mapOf(Music.URI to QuietMediaPlayerTest.URI))
    }

    private lateinit var playlist: Playlist


    @Before
    fun setUp() {
        playlist = Playlist("test_token", musics)
        playlist.playMode = PlayMode.Shuffle
    }

    @After
    fun tearDown() {
        manager.musicPlayer.playlist = Playlist.EMPTY
        manager.musicPlayer.quiet()
    }

    @Test
    fun testPersistPlaylist() = runBlocking {
        manager.musicPlayer.playlist = playlist

        delay(1000)
        val token = KeyValue.get<String>("player_playlist_key_token")
        val musics: List<Music>? = KeyValue.get("player_playlist_key_music_list", object : TypeToken<List<Music>>() {}.type)
        val playMode = KeyValue.get<String>("play_playlist_key_play_mode")
        val current = KeyValue.get<Music>("player_playlist_key_current")

        assertEquals(playlist.token, token)
        assertArrayEquals(playlist.list.toTypedArray(), musics!!.toTypedArray())
        assertEquals(playlist.playMode, PlayMode.from(playMode))
        assertEquals(playlist.current, current)
    }

    @Test
    fun testPlayMusic() = runBlocking {

        delay(200)

        manager.play("test", musics[0], musics)
        delay(1000)
        assertEquals(musics[0], manager.playingMusic.await())

        // let playlist use Sequence play mode
        manager.musicPlayer.playlist.playMode = PlayMode.Sequence

        manager.musicPlayer.playNext()
        delay(1000)
        assertEquals(musics[1], manager.playingMusic.await())


        manager.musicPlayer.playNext()
        delay(1000)
        assertEquals(musics[2], manager.playingMusic.await())


    }

    @Test
    fun testStateChange() = runBlocking {
        manager.musicPlayer.playlist = playlist

        manager.musicPlayer.playPause()
        delay(1000)
        assertEquals(IMediaPlayer.PLAYING, manager.playerState.await())

        manager.musicPlayer.playPause()
        delay(1000)
        assertEquals(IMediaPlayer.PAUSING, manager.playerState.await())

        manager.musicPlayer.quiet()
        delay(1000)
        assertEquals(IMediaPlayer.IDLE, manager.playerState.await())
    }


}