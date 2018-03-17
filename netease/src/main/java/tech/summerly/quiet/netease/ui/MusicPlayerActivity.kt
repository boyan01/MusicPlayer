package tech.summerly.quiet.netease.ui

import android.os.Bundle
import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.netease_activity_music_player.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.component.activities.NoIsolatedActivity
import tech.summerly.quiet.commonlib.fragments.PlayingListFragment
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.constraints.Netease
import tech.summerly.quiet.netease.R

/**
 * Created by summer on 18-2-26
 */
@Route(path = Netease.ACTIVITY_NETASE_PLAYER)
internal class MusicPlayerActivity : NoIsolatedActivity() {

    override val parentPath: String = "/netease/main"

    private val current: Music?
        get() {
            val music = musicPlayer.current
            if (music?.type != MusicType.NETEASE || music.type != MusicType.LOCAL) {
                finish()
                return null
            }
            return music
        }

    private var isUserTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.CommonAppTheme_NoActionBar_TranslucentStatus)
        setContentView(R.layout.netease_activity_music_player)
        initView()
        listenEvent()
    }

    private fun initView() {
        controllerPauseOrPlay.setOnClickListener {
            musicPlayer.playPause()
        }
        controllerSkipNext.setOnClickListener {
            musicPlayer.playNext()
        }
        controllerSkipPrevious.setOnClickListener {
            musicPlayer.playPrevious()
        }
        actionLike.setOnClickListener {
            log { "like :$current" }
        }
        actionAdd.setOnClickListener {

        }
        actionShare.setOnClickListener {

        }
        actionPlaylist.setOnClickListener {
            PlayingListFragment().show(supportFragmentManager, PlayingListFragment.TAG)
        }
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textCurrentPosition.text = progress.toMusicTimeStamp()
                textDuration.text = seekBar.max.toMusicTimeStamp()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isUserTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isUserTracking = false
                musicPlayer.seekTo(seekBar.progress.toLong())
            }
        })
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun listenEvent() = with(MusicPlayerManager) {
        musicChange.observeFilterNull(this@MusicPlayerActivity) { (_, music) ->
            music ?: return@observeFilterNull
            toolbar.title = music.title
            GlideApp.with(this@MusicPlayerActivity).load(music.picUri).into(artWork)
            if (playerState.value == PlayerState.Idle) {
                progressBar.max = music.duration.toInt()
                textDuration.text = music.duration.toMusicTimeStamp()
            }
        }

        playerState.observe(this@MusicPlayerActivity) { state ->
            when (state) {
                PlayerState.Playing -> {
                    controllerPauseOrPlay.setImageResource(R.drawable.common_ic_pause_circle_outline_black_24dp)
                    textDuration.text = musicPlayer.duration.toMusicTimeStamp()
                }
                else -> controllerPauseOrPlay.setImageResource(R.drawable.common_ic_play_arrow_black_24dp)
            }
        }
        position.observeFilterNull(this@MusicPlayerActivity) { (current, total) ->
            lyric.scrollLyricTo(current.toInt())
            textCurrentPosition.text = current.toMusicTimeStamp()
            textDuration.text = total.toMusicTimeStamp()
            if (isUserTracking) {
                return@observeFilterNull
            }
            progressBar.max = total.toInt()
            progressBar.progress = current.toInt()
        }
    }
}