package debug

import android.app.Application
import android.os.Environment
import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicUrlFetcher
import tech.summerly.quiet.commonlib.player.MusicUrlGetter
import java.io.File

/**
 * Created by summer on 18-2-12
 */
internal class DebugAppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseModule(this, "tech.summerly.quiet.commonlib.LibModule")
        BaseModule(this, "tech.summerly.quiet.setting.SettingModule")
        MusicUrlFetcher.addMusicUrlGetter(MusicType.LOCAL, object : MusicUrlGetter {
            override suspend fun getPlayableUrl(music: Music): String? {
                val file = File(Environment.getExternalStorageDirectory(), "test.mp3")
                return file.path
            }
        })
    }
}