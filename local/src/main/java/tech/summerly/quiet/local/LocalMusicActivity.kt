package tech.summerly.quiet.local

import android.os.Build
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.constraints.Local

/**
 * Created by summer on 17-12-21
 */
@Route(path = Local.PATH_LOCAL_MAIN)
class LocalMusicActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.local_activity_main)
    }

}