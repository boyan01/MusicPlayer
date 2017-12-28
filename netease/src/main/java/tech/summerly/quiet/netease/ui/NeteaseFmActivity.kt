package tech.summerly.quiet.netease.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.netease_activity_fm.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.player.BaseMusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.state.PlayerState
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.commonlib.utils.observeFilterNull
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.player.NeteaseFmMusicPlayer

/**
 * author : yangbin10
 * date   : 2017/12/22
 */
class NeteaseFmActivity : BaseActivity() {

    private val musicPlayer: BaseMusicPlayer
        get() = MusicPlayerManager.INSTANCE.getMusicPlayer() as? NeteaseFmMusicPlayer
                ?: NeteaseFmMusicPlayer(this)
                .also { MusicPlayerManager.INSTANCE.setMusicPlayer(it) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_fm)
        listenEvent()
        initPlayer()
        musicPlayer.getPlayingMusic().observeFilterNull(this) {
            GlideApp.with(this).load(it.picUri).into(imageArtwork)
            textArtist.text = it.artistAlbumString()
            textMusicName.text = it.title
            textDuration.text = it.duration.toString()
            seekBar.max = (it.duration / 1000).toInt()
        }
        musicPlayer.playerState.observe(this) {
            when (it) {
                PlayerState.Playing ->
                    buttonPlay.setImageResource(R.drawable.common_ic_pause_circle_outline_black_24dp)
                PlayerState.Pausing ->
                    buttonPlay.setImageResource(R.drawable.common_ic_play_circle_outline_black_24dp)
                PlayerState.Loading -> Unit
            }
        }
        musicPlayer.position.observeFilterNull(this) {
            val progress = (it / 1000).toInt()
            textCurrentPosition.text = progress.toString()
            seekBar.progress = progress
        }
    }

    private fun listenEvent() {
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
    }

    private fun markPlayingAsDislike() {

    }

    //init fm player and start to play
    private fun initPlayer() {
        //TODO
    }
}