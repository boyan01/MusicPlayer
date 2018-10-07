package tech.soit.quiet.ui.service

import android.content.Intent
import android.os.Bundle
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.ui.activity.MusicPlayerActivity
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.main.AppMainActivity


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
            //TODO
            startActivity(Intent(this, AppMainActivity::class.java))
        } else {
            startActivity(Intent(this, MusicPlayerActivity::class.java))
        }
        finish()
    }

}