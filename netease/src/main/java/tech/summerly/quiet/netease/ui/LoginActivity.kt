package tech.summerly.quiet.netease.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.netease_activity_login.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.progressDialog
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi

/**
 * author : yangbin10
 * date   : 2017/12/21
 */
class LoginActivity : BaseActivity() {

    private var jobLogin: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_login)
        listenEvent()
    }

    private fun listenEvent() {
        inputPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptToLogin()
                return@OnEditorActionListener true
            }
            false
        })
        buttonLogin.setOnClickListener {
            attemptToLogin()
        }
    }

    private fun attemptToLogin() {
        if (jobLogin != null) {
            return
        }
        //clear error first
        tilPhone.error = null
        tilPassword.error = null
        //do not need to check is progress dialog showing
        //because we check jobLogin first
        val progressDialog = progressDialog(R.string.netease_hint_login_waitting) {
            setCanceledOnTouchOutside(false)
        }
        progressDialog.show()
        jobLogin = launch {
            val phoneStr = inputPhone.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            if (!isValidPhone(phoneStr) || !isValidPassword(password)) {
                return@launch
            }
            val loginResult = NeteaseCloudMusicApi(this@LoginActivity).login(phoneStr, password)
            if (loginResult.code == 200) {
                loginSuccess()
            } else {
                launch(UI) { inputPhone.error = getString(R.string.netease_error_login_failed) }
            }
            log { "login result $loginResult" }
            jobLogin = null
        }.also {
            //bind progress dialog to login job.
            it.invokeOnCompletion {
                progressDialog.dismiss()
            }
        }
    }

    private fun loginSuccess() {
        finish()
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.isEmpty()) {
            launch(UI) { tilPassword.error = getString(R.string.netease_error_empty_password) }
            return false
        }
        return true
    }

    private fun isValidPhone(phoneStr: String): Boolean {
        if (phoneStr.isEmpty()) {
            launch(UI) { tilPhone.error = getString(R.string.netease_error_empty_phone) }
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        jobLogin?.cancel()
    }
}