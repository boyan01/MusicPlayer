package tech.summerly.quiet.setting

import android.os.Bundle
import android.widget.TextView
import tech.summerly.quiet.commonlib.base.BaseActivity

/**
 * Created by summer on 18-2-12
 */
class SettingMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).also {
            it.text = "Hello world!"
        })
    }

}