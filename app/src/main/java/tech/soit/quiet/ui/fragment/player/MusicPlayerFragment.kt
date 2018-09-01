package tech.soit.quiet.ui.fragment.player

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_music_player.*
import kotlinx.android.synthetic.main.player_content_music_controller.*
import kotlinx.android.synthetic.main.player_content_music_controller.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.subTitle

@LayoutId(R.layout.fragment_music_player)
class MusicPlayerFragment : BaseFragment() {

    companion object {

        const val TAG = "MusicPlayerFragment"

    }

    private var isUserTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicPlayerManager.playerState.observe(this, Observer {
            if (it == IMediaPlayer.PLAYING) {
                buttonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
            } else {
                buttonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
        })
        MusicPlayerManager.playingMusic.observe(this, Observer { music ->
            music ?: return@Observer
            textTitle.text = music.title
            textSubTitle.text = music.subTitle
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        iconUp.setOnClickListener { onBackPressed() }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
//                    MusicPlayerManager.player.mediaPlayer.seekTo(p1.toLong())
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
            MusicPlayerManager.musicPlayer.playlist.playMode = MusicPlayerManager.musicPlayer.playlist.playMode.next()
        }
        buttonPlayPrevious.setOnClickListener {
            MusicPlayerManager.musicPlayer.playPrevious()
        }
        buttonPlayPause.setOnClickListener {
            MusicPlayerManager.musicPlayer.playPause()
        }
        buttonPlayNext.setOnClickListener {
            MusicPlayerManager.musicPlayer.playNext()
        }
        buttonPlayerPlaylist.setOnClickListener {
            //TODO
        }

    }

    private fun setPlayingMusic(music: Music) {
        textTitle.text = music.title
        textSubTitle.text = music.subTitle
//        val bitmap = PictureModel.with(music.attach[Music.PIC_URI]).get()
//        imageArtwork.setImageBitmap(bitmap)
//        val blur = bitmap.blur()
//        imageBackground.setImageBitmap(blur)
    }


}