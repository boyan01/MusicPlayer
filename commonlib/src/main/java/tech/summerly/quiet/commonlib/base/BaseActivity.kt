package tech.summerly.quiet.commonlib.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.Job

/**
 *  Created by summer on 17-12-17
 * as backup
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    protected val job = Job()
}