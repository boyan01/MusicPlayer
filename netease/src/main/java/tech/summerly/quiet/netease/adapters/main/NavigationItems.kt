package tech.summerly.quiet.netease.adapters.main

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.netease_content_nav_item.view.*
import kotlinx.android.synthetic.main.netease_item_navigation.view.*
import org.jetbrains.anko.startActivity
import tech.summerly.quiet.commonlib.component.activities.AppTask
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.intransaction
import tech.summerly.quiet.commonlib.utils.support.SimpleTypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.constraints.Download
import tech.summerly.quiet.constraints.Player
import tech.summerly.quiet.netease.NeteaseFmPlaylist
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.ui.NeteaseDailyRecommendActivity
import tech.summerly.quiet.netease.ui.NeteaseRecordActivity


internal object Navigation {


    val fm = R.string.netease_nav_title_fm to R.drawable.netease_ic_radio_black_24dp

    val daily = R.string.netease_nav_title_daily to R.drawable.netease_ic_today_black_24dp

    val download = R.string.netease_nav_title_download to R.drawable.netease_ic_file_download_black_24dp

    val record = R.string.netease_nav_title_latest to R.drawable.netease_ic_access_time_black_24dp

}

internal class NavigationViewBinder : SimpleTypedBinder<Navigation>() {

    override val layoutId: Int get() = R.layout.netease_item_navigation

    override fun onBindViewHolder(holder: ViewHolder, item: Navigation) = with(holder.itemView) {
        navFm.setData(Navigation.fm)
        navFm.setOnClickListener {
            it.navigationToFmPlayer()
        }
        navDaily.setData(Navigation.daily)
        navDaily.setOnClickListener {
            context.startActivity<NeteaseDailyRecommendActivity>()
        }
        navDownload.setData(Navigation.download)
        navDownload.setOnClickListener {
            val fragment = ARouter.getInstance().build(Download.DOWNLOAD_MAIN).navigation() as? Fragment
                    ?: return@setOnClickListener
            val activity = AppTask.topStackActivity ?: return@setOnClickListener
            activity.supportFragmentManager.intransaction {
                add(android.R.id.content, fragment)
                addToBackStack(null)
            }
        }
        navRecord.setData(Navigation.record)
        navRecord.setOnClickListener {
            context.startActivity<NeteaseRecordActivity>()
        }
    }

    //make image view foreground to match background
    private fun ImageView.setInteractive() {
//        setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> drawable.setTint(context.getAttrColor(R.attr.colorTextPrimaryInverse))
//                MotionEvent.ACTION_UP -> drawable.setTint(context.getAttrColor(R.attr.colorPrimary))
//            }
//            false
//        }
    }

    private fun View.setData(pair: Pair<Int, Int>) {
        text.setText(pair.first)
        image.setImageResource(pair.second)
        image.setInteractive()
    }

    private fun View.navigationToFmPlayer() {
        val musicPlayer = MusicPlayerManager.player
        if (musicPlayer.playlist.token != NeteaseFmPlaylist.TOKEN) {
            musicPlayer.playlist = NeteaseFmPlaylist()
        }
        val fragmentName = Player.FRAGMENT_FM_PLAYER_NORMAL
        val fragment = ARouter.getInstance().build(fragmentName).navigation() as Fragment?
                ?: return
        (context as AppCompatActivity).supportFragmentManager.intransaction {
            replace(android.R.id.content, fragment, fragmentName)
            addToBackStack(fragmentName)
        }
        playFmMusic()
    }

    private fun playFmMusic() {
        if (MusicPlayerManager.player.playlist.type != PlayerType.FM) {
            return
        }
        val state = MusicPlayerManager.player.mediaPlayer.getPlayerState()
        if (state == PlayerState.Playing || state == PlayerState.Preparing) {
            return
        } else if (state == PlayerState.Pausing) {
            MusicPlayerManager.player.mediaPlayer.start()
        } else {
            MusicPlayerManager.player.playNext()
        }
    }

}