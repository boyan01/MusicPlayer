package tech.summerly.quiet.commonlib.player.interaction

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.MusicType

/**
 * Created by summer on 18-3-5
 */
internal class PendingIntentProxyActivity : BaseActivity() {

    companion object {

        const val ACTION_TO_PLAYER = "to_music_player"

        const val KEY_ACTION = "action"

        const val KEY_TYPE = "quiet_type"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action = intent.getStringExtra(KEY_ACTION)
        when (action) {
            ACTION_TO_PLAYER -> {
                val type = intent.getStringExtra(KEY_TYPE)
                val url = if (type == MusicType.NETEASE_FM.name) "/netease/fm" else "/netease/player"
                ARouter.getInstance().build(url).navigation()
            }
            else -> Unit
        }
        finish()
    }
}