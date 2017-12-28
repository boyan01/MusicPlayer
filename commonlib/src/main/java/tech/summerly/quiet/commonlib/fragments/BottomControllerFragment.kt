package tech.summerly.quiet.commonlib.fragments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.common_fragment_controller_bottom.view.*
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.BaseMusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.state.PlayerState
import tech.summerly.quiet.commonlib.utils.*

/**
 * Created by summer on 17-12-17
 */
class BottomControllerFragment : BaseFragment() {

    private val musicPlayer: BaseMusicPlayer
        get() = MusicPlayerManager.INSTANCE.getMusicPlayer()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.common_fragment_controller_bottom, container, false)
        listenEvent(root)
        return root
    }

    override fun onStart() {
        super.onStart()
        musicPlayer.playerState.observeFilterNull(this) {
            setControllerState(it)
        }
        musicPlayer.getPlayingMusic().observe(this) {
            log { "更新底部控制栏: $it" }
            updateMusicInfo(it)
        }
    }

    private fun listenEvent(root: View) = with(root) {
        controllerPauseOrPlay.setOnClickListener {
            musicPlayer.playPause()
        }
        controllerSkipNext.setOnClickListener {
            musicPlayer.playNext()
        }
        controllerSkipPrevious.setOnClickListener {
            musicPlayer.playPrevious()
        }
        this.setOnClickListener {
            //TODO
        }
    }

    private fun setControllerState(state: PlayerState) = runWithRoot {
        progressPlayPause.gone()
        controllerPauseOrPlay.visible()
        when (state) {
            PlayerState.Playing -> controllerPauseOrPlay.setImageResource(R.drawable.common_ic_pause_circle_outline_black_24dp)
            PlayerState.Pausing -> controllerPauseOrPlay.setImageResource(R.drawable.common_ic_play_circle_outline_black_24dp)
            PlayerState.Loading -> {
                progressPlayPause.visible()
                controllerPauseOrPlay.gone()
            }
        }
    }

    private fun updateMusicInfo(music: Music?) = runWithRoot {
        //首先根据music是否为空来控制底部控制fragment的显示与否。
        if (music == null) {
            (activity as? BottomControllerContainer)?.hideBottomController()
            return@runWithRoot
        }
        (activity as? BottomControllerContainer)?.showBottomController()
        //更新音乐信息
        musicTitle.text = music.title
        musicSubTitle.text = music.artistAlbumString()
        GlideApp.with(this).load(music.picUri).into(artWork)
    }

    interface BottomControllerContainer {
        companion object {
            /**
             * equal with [R.string.common_tag_fragment_bottom_controller]
             */
            @Suppress("unused")
            const val TAG_COMMON_TAG_FRAGMENT_CONTROLLER = "bottom_fragment_tag"
        }

        /**
         * 隐藏当前界面的底部控制栏
         */
        fun hideBottomController() {
            if (this !is AppCompatActivity) {
                return
            }
            TransitionManager.beginDelayedTransition(findViewById(android.R.id.content))
            val fragment = supportFragmentManager.findFragmentByTag(getString(R.string.common_tag_fragment_bottom_controller))
            with(fragment.view) {
                this ?: return@with
                layoutParams.height = 0
                parent.requestLayout()
            }
        }

        /**
         * 显示当前界面的底部控制栏
         */
        fun showBottomController() {
            if (this !is AppCompatActivity) {
                return
            }
            TransitionManager.beginDelayedTransition(findViewById(android.R.id.content))
            val fragment = supportFragmentManager.findFragmentByTag(getString(R.string.common_tag_fragment_bottom_controller))
            with(fragment.view) {
                this ?: return@with
                layoutParams.height = dip(64)
                parent.requestLayout()
            }
        }
    }
}