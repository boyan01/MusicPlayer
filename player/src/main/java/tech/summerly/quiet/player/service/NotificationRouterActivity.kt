package tech.summerly.quiet.player.service

import android.os.Bundle
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.component.activities.AppTask
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.utils.intransaction
import tech.summerly.quiet.constraints.Main
import tech.summerly.quiet.constraints.Player


internal class NotificationRouterActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = MusicPlayerManager.player.playlist.type
        val path = when (type) {
            PlayerType.NORMAL -> Player.FRAGMENT_MUSIC_PLAYER
            PlayerType.FM -> Player.FRAGMENT_FM_PLAYER_NORMAL
        }
        navigationToPlayer(path)

    }

    private fun navigationToPlayer(path: String) {
        val active = AppTask.getActiveActivities()
        if (active.isEmpty()) {
            //navigation to MainActivity
            //TODO and open playing fragment
            ARouter.getInstance().build(Main.ACTIVITY_MAIN).navigation()
        } else {
            val topStack = active.valueAt(active.size - 1)!!
            val find = topStack.supportFragmentManager.findFragmentByTag(path)
            if (find == null || !find.isVisible) {
                val fragment = find ?: ARouter.getInstance().build(path).navigation() as Fragment
                topStack.supportFragmentManager.intransaction {
                    replace(android.R.id.content, fragment, path)
                    addToBackStack(path)
                }
            }
        }
        finish()
    }

}