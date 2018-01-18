package tech.summerly.quiet.netease.ui

import android.os.Bundle
import android.transition.TransitionManager
import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.netease_activity_fm.*
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.act
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.BaseMusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi

/**
 * activity of personal fm radio
 */
@Route(path = "/netease/fm")
class NeteaseFmActivity : BaseActivity() {

    private val musicPlayer: BaseMusicPlayer
        get() = MusicPlayerManager.musicPlayer(MusicType.NETEASE_FM)

    private val playerManager: MusicPlayerManager
        get() = MusicPlayerManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_fm)
        initView()
        listenEvent()
        playerManager.playingMusic.observeFilterNull(this) {
            UI.submit {
                val height = (getScreenWidth() * 0.8f).toInt()
                val picture = GlideApp.with(act).asBitmap().loadAndGet(it.getPictureUrl(), height, height)
                        ?: return@submit
                imageArtwork.setImageBitmap(picture)
                val blur = FastBlur.doBlur(picture, 24, false)
                imageBackground.setImageBitmap(blur)
            }
            toolbar.title = it.title
            textArtist.text = it.artistAlbumString()
            textMusicName.text = it.title
            textDuration.text = it.duration.toMusicTimeStamp()
            seekBar.max = (it.duration).toInt()
            showLyric(it)
            updateMenuItems(it)
        }
        playerManager.playerState.observe(this) {
            when (it) {
                PlayerState.Playing ->
                    buttonPlay.setImageResource(R.drawable.common_ic_pause_circle_outline_black_24dp)
                PlayerState.Pausing ->
                    buttonPlay.setImageResource(R.drawable.common_ic_play_circle_outline_black_24dp)
                PlayerState.Loading -> Unit
                else -> {

                }
            }
        }
        playerManager.position.observeFilterNull(this) {
            lyricView.scrollLyricTo(it.toInt())
            if (isSeekBarTracking) { //do not change seekBar progress when user is tracking touch
                return@observeFilterNull
            }
            val progress = it.toInt()
            textCurrentPosition.text = progress.toMusicTimeStamp()
            seekBar.progress = progress
        }
        playMusicIfNecessary()
    }

    private fun showLyric(music: Music) = launch(UI) {
        lyricView.setLyricText(NeteaseCloudMusicApi().getLyric(music.id))
    }

    private fun playMusicIfNecessary() {
        if (musicPlayer.corePlayer.getState() == PlayerState.Pausing) {
            musicPlayer.corePlayer.start()
        }
        if (musicPlayer.corePlayer.getState() != PlayerState.Playing) {
            //start player
            val current = musicPlayer.current
            if (current != null) {
                musicPlayer.play(current)
            } else {
                musicPlayer.playNext()
            }
        }
    }

    private fun initView() {
        toolbar.inflateMenu(R.menu.netease_menu_fm_player)
        toolbar.overflowIcon?.setTint(color(R.color.common_text_primary_dark_background))
//        navigationViewPlaceHolder.layoutParams.height = getNavigationBarHeight()
    }

    /**
     * update toolbar menu items when music has been changed
     */
    private fun updateMenuItems(music: Music) = with(toolbar.menu) {
        findItem(R.id.netease_menu_fm_album)
                .title = getString(R.string.netease_menu_fm_album) + ": ${music.album.name}"
        findItem(R.id.netease_menu_fm_artist)
                .title = getString(R.string.netease_menu_fm_artist) + ": ${music.artist.joinToString("/") { it.name }}"
    }

    private fun listenEvent() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.netease_menu_fm_add_playlist -> {

                }
                R.id.netease_menu_fm_share -> {

                }
                R.id.netease_menu_fm_artist -> {

                }
                R.id.netease_menu_fm_album -> {

                }
                R.id.netease_menu_fm_download -> {

                }
                R.id.netease_menu_fm_quality -> {

                }
                R.id.netease_menu_fm_timer -> {

                }
            }
            true
        }
        buttonDelete.setOnClickListener {
            markPlayingAsDislike()
        }
        buttonLike.setOnClickListener {

        }

        buttonPlay.setOnClickListener {
            musicPlayer.playPause()
        }
        buttonNext.setOnClickListener {
            musicPlayer.playNext()
        }
        buttonComment.setOnClickListener {

        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicPlayer.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTracking = false
            }

        })
        playerInfo.setOnClickListener {
            TransitionManager.beginDelayedTransition(playerInfo)
            if (lyricView.isVisible) {
                lyricView.invisible()
                imageArtwork.visible()
                textMusicName.visible()
                textArtist.visible()
            } else {
                lyricView.visible()
                imageArtwork.invisible()
                textMusicName.invisible()
                textArtist.invisible()
            }
        }
    }

    private var isSeekBarTracking = false

    private fun markPlayingAsDislike() {

    }

}