package tech.summerly.quiet.player.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.player_fragment_bottom_controller.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.component.callback.BottomControllerHost
import tech.summerly.quiet.commonlib.fragments.PlayingListFragment
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.constraints.Player
import tech.summerly.quiet.player.R

@Route(path = Player.FRAGMENT_BOTTOM_CONTROLLER)
internal class BottomControllerFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MusicPlayerManager.playerState.observeFilterNull(this) {
            setControllerState(it)
        }
        MusicPlayerManager.playingMusic.observe(this) {
            updateMusicInfo(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_fragment_bottom_controller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener {
            onControllerClicked(MusicPlayerManager.player.playlist.type)
        }
        controllerPauseOrPlay.setOnClickListener {
            MusicPlayerManager.player.playPause()
        }
        controllerPlaylist.setOnClickListener {
            activity?.supportFragmentManager?.let {
                PlayingListFragment().show(it, string(tech.summerly.quiet.commonlib.R.string.common_tag_fragment_playing_list))
            }
        }
    }

    private fun onControllerClicked(type: PlayerType) {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager == null) {
            log(LoggerLevel.ERROR) { "controller clicked , but do not attach to parent activity!!" }
            return
        }
        when (type) {
            PlayerType.FM -> {
                val fmPlayer = Player.FRAGMENT_FM_PLAYER_NORMAL
                val fragment = fragmentManager.findFragmentByTag(fmPlayer)
                        ?: ARouter.getInstance().build(fmPlayer).navigation() as Fragment?
                        ?: return
                fragmentManager.intransaction {
                    replace(android.R.id.content, fragment, fmPlayer)
                    addToBackStack(fmPlayer)
                }
            }
            PlayerType.NORMAL -> {
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

    @Synchronized
    private fun setControllerState(state: PlayerState) = runWithRoot {
        progressPlayPause.invisible()
        controllerPauseOrPlay.visible()
        when (state) {
            PlayerState.Playing -> {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_pause_black_24dp)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    controllerPauseOrPlay.tooltipText = string(R.string.player_pause)
                }
            }
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
            else -> {
                controllerPauseOrPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    controllerPauseOrPlay.tooltipText = string(R.string.player_play)
                }
            }
        }
    }

    private fun updateMusicInfo(music: IMusic?) = runWithRoot {
        val host = getControllerHost() ?: return@runWithRoot
        //首先根据music是否为空来控制底部控制fragment的显示与否。
        if (music == null) {
            host.hideBottomController()
        } else {
            host.showBottomController()
            //更新音乐信息
            musicTitle.text = music.title
            musicSubTitle.text = music.artistAlbumString()
            GlideApp.with(this).load(music.getPictureUrl()).into(artWork)
        }
    }

    private fun getControllerHost(): BottomControllerHost? {
        if (activity is BottomControllerHost) {
            return activity as BottomControllerHost
        } else if (parentFragment is BottomControllerHost) {
            return parentFragment as BottomControllerHost
        } else {
            return null
        }
    }

}