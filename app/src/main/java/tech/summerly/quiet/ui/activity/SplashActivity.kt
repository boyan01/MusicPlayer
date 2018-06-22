package tech.summerly.quiet.ui.activity

import android.content.Intent
import android.os.Bundle
import tech.summerly.quiet.commonlib.base.BaseActivity

/**
 * application splash
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}