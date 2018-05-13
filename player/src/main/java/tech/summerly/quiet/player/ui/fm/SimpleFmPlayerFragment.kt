package tech.summerly.quiet.player.ui.fm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.player_fragment_fm_simple.view.*
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.getPictureUrl
import tech.summerly.quiet.commonlib.utils.getScreenWidth
import tech.summerly.quiet.commonlib.utils.image.PictureModel
import tech.summerly.quiet.commonlib.utils.observeFilterNull
import tech.summerly.quiet.constraints.Player
import tech.summerly.quiet.player.R
import tech.summerly.quiet.player.ui.InsetsFragment

@Route(path = Player.FRAGMENT_FM_PLAYER_SIMPLE)
class SimpleFmPlayerFragment : InsetsFragment() {

    private val musicPlayer get() = MusicPlayerManager.musicPlayer(MusicType.NETEASE_FM)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        MusicPlayerManager.musicChange.observeFilterNull(this) {
            it.second?.let { changeMusic(it) }
        }
        MusicPlayerManager.playerState.observeFilterNull(this, this::changeState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_fragment_fm_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        buttonPlayPause.setOnClickListener {
            musicPlayer.playPause()
        }
        buttonPlayNext.setOnClickListener {
            musicPlayer.playNext()
        }
        buttonLike.setOnClickListener {
            //TODO
        }
    }


    private fun changeMusic(music: Music) = withRoot {
        textTitle.text = music.title
        val width = (getScreenWidth() * 0.4f).toInt()
        val artwork = PictureModel.with(music.getPictureUrl()).get(imageArtwork, true, width, width)
        imageArtwork.setImageBitmap(artwork)
    }

    private fun changeState(state: PlayerState): Unit = withRoot {
        when (state) {
            PlayerState.Playing -> {
                buttonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
            }
            else -> {
                buttonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
        }
    }

}