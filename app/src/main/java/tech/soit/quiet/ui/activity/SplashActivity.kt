package tech.soit.quiet.ui.activity

import android.content.Intent
import android.os.Bundle
import tech.soit.quiet.ui.activity.base.BaseActivity

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