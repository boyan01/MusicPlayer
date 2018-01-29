package tech.summerly.quiet.commonlib.fragments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.common_fragment_controller_bottom.view.*
import org.jetbrains.anko.dimen
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.musicPlayer
import tech.summerly.quiet.commonlib.utils.*

/**
 * Created by summer on 17-12-17
 */
open class BottomControllerFragment : BaseFragment() {

    private val playerManager: MusicPlayerManager
        get() = MusicPlayerManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.common_fragment_controller_bottom, container, false)
        listenEvent(root)
        return root
    }

    override fun onStart() {
        super.onStart()
        playerManager.playerState.observeFilterNull(this) {
            setControllerState(it)
        }
        playerManager.playingMusic.observe(this) {
            updateMusicInfo(it)
        }
    }

    private fun listenEvent(root: View) = with(root) {
        setOnClickListener { view ->
            playerManager.playingMusic.value?.let {
                onControllerClick(view, it)
            }
        }
        controllerPauseOrPlay.setOnClickListener {
            playerManager.musicPlayer().playPause()
        }
        controllerPlaylist.setOnClickListener {
            activity?.supportFragmentManager?.let {
                PlayingListFragment().show(it, string(R.string.common_tag_fragment_playing_list))
            }
        }
    }

    @Synchronized
    private fun setControllerState(state: PlayerState) = runWithRoot {
        progressPlayPause.invisible()
        controllerPauseOrPlay.visible()
        when (state) {
            PlayerState.Playing -> controllerPauseOrPlay.setImageResource(R.drawable.common_ic_pause_circle_outline_black_24dp)
            PlayerState.Loading -> {
                controllerPauseOrPlay.setImageResource(R.drawable.common_ic_pause_circle_outline_black_24dp)
                // delay 500ms to display load progress bar
                handler?.postDelayed({
                    if (!isDetached && view != null && musicPlayer.corePlayer.getState() == PlayerState.Loading) {
                        progressPlayPause.visible()
                        controllerPauseOrPlay.gone()
                    }
                }, 500)
            }
            else -> controllerPauseOrPlay.setImageResource(R.drawable.common_ic_play_circle_outline_black_24dp)
        }
    }

    protected open fun onControllerClick(view: View, music: Music) {
        when (music.type) {
            MusicType.NETEASE_FM -> {
                ARouter.getInstance().build("/netease/fm").navigation()
            }
            else -> {
                //todo
            }
        }
    }

    private fun updateMusicInfo(music: Music?) {
        //首先根据music是否为空来控制底部控制fragment的显示与否。
        val root = view ?: return
        if (music == null) {
            (activity as? BottomControllerContainer)?.hideBottomController()
            return
        }
        (activity as? BottomControllerContainer)?.showBottomController()
        //更新音乐信息
        root.musicTitle.text = music.title
        root.musicSubTitle.text = music.artistAlbumString()
        GlideApp.with(this).load(music.getPictureUrl()).into(root.artWork)
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
                layoutParams.height = dimen(R.dimen.common_height_bottom_controller)
                parent.requestLayout()
            }
        }
    }
}