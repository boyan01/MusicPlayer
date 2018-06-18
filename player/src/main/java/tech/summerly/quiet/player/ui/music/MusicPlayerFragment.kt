package tech.summerly.quiet.player.ui.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.player_content_music_controller.view.*
import kotlinx.android.synthetic.main.player_fragment_music.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.fragments.PlayingListFragment
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.getPictureUrl
import tech.summerly.quiet.commonlib.utils.image.PictureModel
import tech.summerly.quiet.commonlib.utils.image.blur
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.commonlib.utils.observeFilterNull
import tech.summerly.quiet.commonlib.utils.toMusicTimeStamp
import tech.summerly.quiet.constraints.Player
import tech.summerly.quiet.player.R

@Route(path = Player.FRAGMENT_MUSIC_PLAYER)
class MusicPlayerFragment : BaseFragment() {


    private var isUserTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicPlayerManager.position.observeFilterNull(this) { (current, total) ->
            if (isUserTracking) {
                return@observeFilterNull
            }
            withRoot {
                textCurrentPosition.text = current.toMusicTimeStamp()
                textDuration.text = total.toMusicTimeStamp()
                seekBar.max = total.toInt()
                seekBar.progress = current.toInt()
            }
        }
        MusicPlayerManager.playerState.observeFilterNull(this) {
            withRoot {
                val res = when (it) {
                    PlayerState.Playing -> R.drawable.ic_pause_black_24dp
                    else -> R.drawable.ic_play_arrow_black_24dp
                }
                buttonPlayPause.setImageResource(res)
            }
        }
        MusicPlayerManager.playMode.observeFilterNull(this) {
            withRoot {
                buttonPlayMode.setImageResource(it.drawableRes())
            }
        }
        MusicPlayerManager.playingMusic.observe(this) {
            if (it == null) {
                closeSelf()
            } else {
                setPlayingMusic(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_fragment_music, container, false).also {
            it.setOnTouchListener { _, _ -> true }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        iconUp.setOnClickListener { closeSelf() }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    MusicPlayerManager.player.mediaPlayer.seekTo(p1.toLong())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                isUserTracking = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                isUserTracking = false
            }
        })
        buttonPlayMode.setOnClickListener {
            MusicPlayerManager.player.playMode = MusicPlayerManager.player.playMode.next()
        }
        buttonPlayPrevious.setOnClickListener {
            MusicPlayerManager.player.playPrevious()
        }
        buttonPlayPause.setOnClickListener {
            MusicPlayerManager.player.playPause()
        }
        buttonPlayNext.setOnClickListener {
            MusicPlayerManager.player.playNext()
        }
        buttonPlayerPlaylist.setOnClickListener {
            PlayingListFragment().show(fragmentManager, PlayingListFragment.TAG)
        }

    }

    private fun setPlayingMusic(music: IMusic) = withRoot {
        textTitle.text = music.title
        textSubTitle.text = music.artistAlbumString()
        val bitmap = PictureModel.with(music.getPictureUrl()).get()
        imageArtwork.setImageBitmap(bitmap)
        val blur = bitmap.blur()
        imageBackground.setImageBitmap(blur)
    }


}