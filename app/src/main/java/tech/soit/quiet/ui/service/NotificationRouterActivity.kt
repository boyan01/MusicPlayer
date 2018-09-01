package tech.soit.quiet.ui.service

import android.content.Intent
import android.os.Bundle
import tech.soit.quiet.AppContext
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.main.MainActivity
import tech.soit.quiet.ui.fragment.UnimplementedFragment
import tech.soit.quiet.utils.component.AppTask


/**
 * RouterActivity for Notification to open player fragment
 *
 * @author YangBin
 * @date 2018/8/25
 */
class NotificationRouterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pl = MusicPlayerManager.musicPlayer.playlist.token

        if (pl == Playlist.TOKEN_FM) {
            navigationToPlayer("TODO")
        } else {
            navigationToPlayer("TODO")
        }
    }

    private fun navigationToPlayer(tag: String) {
        val active = AppTask.getActiveActivities()
        if (active.isEmpty()) {
            //navigation to MainActivity
            val intent = Intent(AppContext, MainActivity::class.java)
            intent.putExtra("openPlayingFragment", true)
            AppContext.startActivity(intent)
        } else {
            val topStack = active.valueAt(active.size - 1)!!
            topStack.navigationTo(tag) { UnimplementedFragment() }
        }
        finish()
    }

}