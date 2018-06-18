package tech.summerly.quiet.commonlib.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
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
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.player.PlayerType.FM
import tech.summerly.quiet.commonlib.player.PlayerType.NORMAL
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.constraints.Player

/**
 * Created by summer on 17-12-17
 */
@Deprecated("see more to play module")
open class BottomControllerFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.common_fragment_controller_bottom, container, false)
        listenEvent(root)
        return root
    }

    override fun onStart() {
        super.onStart()
        MusicPlayerManager.playerState.observeFilterNull(this) {
            setControllerState(it)
        }
        MusicPlayerManager.playingMusic.observe(this) {
            updateMusicInfo(it)
        }
    }

    private fun listenEvent(root: View) = with(root) {
        setOnClickListener { view ->
            onControllerClick(view, MusicPlayerManager.player.playlist.type)
        }
        controllerPauseOrPlay.setOnClickListener {
            MusicPlayerManager.player.playPause()
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
            PlayerState.Playing -> controllerPauseOrPlay.setImageResource(R.drawable.ic_pause_black_24dp)
            PlayerState.Preparing -> {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                // delay 500ms to display load progress bar
                handler?.postDelayed({
                    if (!isDetached && view != null
                            && MusicPlayerManager.player.mediaPlayer.getPlayerState() == PlayerState.Preparing) {
                        progressPlayPause.visible()
                        controllerPauseOrPlay.gone()
                    }
                }, 500)
            }
            else -> controllerPauseOrPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }
    }

    protected open fun onControllerClick(view: View, type: PlayerType) {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager == null) {
            log(LoggerLevel.ERROR) { "controller clicked , but do not attach to parent activity!!" }
            return
        }
        when (type) {
            FM -> {
                val fmPlayer = Player.FRAGMENT_FM_PLAYER_NORMAL
                val fragment = ARouter.getInstance().build(fmPlayer).navigation() as Fragment?
                        ?: return
                fragmentManager.intransaction {
                    replace(android.R.id.content, fragment, fmPlayer)
                    addToBackStack(fmPlayer)
                }
            }
            NORMAL -> {
//                ARouter.getInstance().build("/netease/player").navigation()
                val path = Player.FRAGMENT_MUSIC_PLAYER
                val fragment = fragmentManager.findFragmentByTag(path)
                        ?: ARouter.getInstance().build(path).navigation() as Fragment? ?: return
                fragmentManager.intransaction {
                    replace(android.R.id.content, fragment, path)
                    addToBackStack(path)
                }
            }
        }
    }

    private fun updateMusicInfo(music: IMusic?) {
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
        GlideApp.with(this).load(music.artwork).into(root.artWork)
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
         * 隐藏当前界面的底部控制栏            find<View>(R.id.bottomPlayerContainer)

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