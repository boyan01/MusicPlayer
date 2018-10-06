package tech.soit.quiet.ui.activity.main

import android.os.Bundle
import android.widget.FrameLayout
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.home.MainMusicFragment

/**
 * the main activity of application
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frame = FrameLayout(this)
        frame.id = R.id.content
        setContentView(frame)
    }

}
