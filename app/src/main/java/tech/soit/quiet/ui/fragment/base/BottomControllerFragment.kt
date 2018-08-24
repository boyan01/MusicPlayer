package tech.soit.quiet.ui.fragment.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.content_bottom_controller.*
import kotlinx.android.synthetic.main.fragment_bottom_controller.*
import kotlinx.android.synthetic.main.fragment_bottom_controller.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.observeNonNull
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.subTitle
import tech.soit.quiet.viewmodel.MusicControllerViewModel

/**
 * a fragment Template which holder a BottomMusicController
 */
@DisableLayoutInject
open class BottomControllerFragment : BaseFragment() {

    private val controllerViewModel by lazyViewModel<MusicControllerViewModel>()

    final override fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val uiWithBottomController = inflater.inflate(R.layout.fragment_bottom_controller, container, false)

        val ui = onCreateView3(inflater, container, savedInstanceState)
        if (ui != null) {
            uiWithBottomController.fragmentContentHolder.addView(ui)
        }
        return uiWithBottomController
    }

    /**
     * @see onCreateView
     */
    open fun onCreateView3(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controllerViewModel.playingMusic.observe(this, Observer { playing ->
            TransitionManager.beginDelayedTransition(fragmentBottomControllerLayout)
            if (playing == null) {
                bottomControllerLayout.isGone = true
            } else {
                bottomControllerLayout.isVisible = true
                updateBottomController(playing)
            }
        })
        controllerViewModel.playerState.observeNonNull(this) { state ->
            if (state == IMediaPlayer.PLAYING) {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_pause_black_24dp)
                controllerPauseOrPlay.contentDescription = string(R.string.pause)
            } else {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                controllerPauseOrPlay.contentDescription = string(R.string.play)
            }
        }
        bottomControllerLayout.setOnClickListener {
            log { "on bottom controller clicked" }
        }
        controllerPauseOrPlay.setOnClickListener {
            controllerViewModel.pauseOrPlay()
        }
    }

    /**
     * update BottomController UI
     */
    private fun updateBottomController(playing: Music) {
        //更新音乐信息
        musicTitle.text = playing.title
        musicSubTitle.text = playing.subTitle

        val picUri = playing.attach[Music.PIC_URI]
        if (picUri != null) {
            ImageLoader.with(this).load(picUri).into(artWork)
        } else {
            artWork.setImageDrawable(ColorDrawable(requireContext().attrValue(R.attr.colorPrimary)))
        }
    }

}