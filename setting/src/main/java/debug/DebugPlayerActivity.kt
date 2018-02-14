package debug

import android.Manifest
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.setting_activity_debug.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.requestPermission
import tech.summerly.quiet.setting.R

/**
 * Created by summer on 18-2-12
 */
internal class DebugPlayerActivity : BaseActivity() {


    private val player: CoreMediaPlayer = CoreMediaPlayer()

    private val music = Music(
            0L,
            "",
            emptyList(),
            Album(0L, "", "", MusicType.LOCAL),
            "",
            MusicType.LOCAL,
            0L,
            0L,
            mutableListOf(MusicUri(0, "sdcard/test.mp3", Long.MAX_VALUE)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity_debug)

        button1.setOnClickListener {
            asyncUI {
                val b = requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                if (b) {
                    player.play(music)
                }
            }
        }

        button2.setOnClickListener {
            ARouter.getInstance().build("/setting/main").navigation()
        }

    }

}