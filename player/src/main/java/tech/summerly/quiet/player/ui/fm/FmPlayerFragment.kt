package tech.summerly.quiet.player.ui.fm

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.player_content_fm_controller.*
import kotlinx.android.synthetic.main.player_fragment_fm.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.commonlib.utils.image.PictureModel
import tech.summerly.quiet.commonlib.utils.image.blur
import tech.summerly.quiet.constraints.Player
import tech.summerly.quiet.player.R

@Route(path = Player.FRAGMENT_FM_PLAYER_NORMAL)
class FmPlayerFragment : BaseFragment() {

    //is user tracking the seekBar
    private var isSeekBarTracking = false

    private val player: MusicPlayer get() = MusicPlayerManager.player

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        MusicPlayerManager.musicChange.observeFilterNull(this) {
            onMusicChange(it.first, it.second)
        }
        MusicPlayerManager.playerState.observeFilterNull(this, onPlayerStateChange)
        MusicPlayerManager.position.observeFilterNull(this) {
            onPositionChange(it.first, it.second)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_fragment_fm, container, false)
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        buttonPlay.setOnClickListener {
            player.playPause()
        }
        buttonNext.setOnClickListener {
            player.playNext()
        }
        buttonLike.setOnClickListener {

        }
        buttonDelete.setOnClickListener {

        }
        buttonComment.setOnClickListener {

        }
        toolbar.navigationIcon = drawable(R.drawable.ic_arrow_back_white_24dp, context.getAttrColor(R.attr.colorTextPrimaryInverse))
        toolbar.setNavigationOnClickListener {
            closeSelf()
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.mediaPlayer.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeekBarTracking = false
            }
        })
        Unit
    }

    override fun onResume() {
        super.onResume()
        //close self if current play type isn't FM
        if (player.playlist.type != PlayerType.FM) {
            closeSelf()
        }
    }


    private val onMusicChange = fun(_: Music?, new: Music?) {
        if (new == null) {
            return
        }
        withRoot {
            textArtist.text = new.artistAlbumString()
            textMusicName.text = new.title

            val height = (getScreenWidth() * 0.8f).toInt()
            val artwork = PictureModel.with(new.getPictureUrl()).get(imageArtwork, width = height, height = height)
            imageArtwork.setImageBitmap(artwork)
            val blur = artwork.blur()
            imageBackground.setImageBitmap(blur)
        }
    }

    private val onPlayerStateChange = fun(state: PlayerState) {
        when (state) {
            PlayerState.Playing -> {
                buttonPlay.setImageResource(R.drawable.ic_pause_black_24dp)
            }
            PlayerState.Preparing -> {
                //todo 准备中
            }
            else -> {
                buttonPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
        }
    }

    private fun onPositionChange(position: Long, total: Long) {
        if (isSeekBarTracking) {
            return
        }
        withRoot {
            textCurrentPosition.text = position.toMusicTimeStamp()
            textDuration.text = total.toMusicTimeStamp()
            seekBar.progress = position.toInt()
            seekBar.max = total.toInt()
        }
    }

}