package tech.summerly.quiet.netease.ui

import android.view.View
import org.jetbrains.anko.support.v4.startActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment

/**
 * Created by summer on 18-1-14
 */
class NeteaseBottomControllerFragment : BottomControllerFragment() {

    override fun onControllerClick(view: View, music: Music) {
        when (music.type) {
            MusicType.NETEASE_FM -> {
                startActivity<NeteaseFmActivity>()
            }
            else -> super.onControllerClick(view, music)
        }
    }
}