package tech.summerly.quiet

import android.os.Bundle
import kotlinx.serialization.json.JSON
import tech.summerly.quiet.commonlib.base.BaseActivity

/**
 * the main activity of application
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
