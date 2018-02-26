@file:Suppress("DEPRECATION")

package tech.summerly.quiet.netease.ui

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.netease_activity_login.*
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.progressDialog
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.persistence.NeteasePreference
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import tech.summerly.quiet.service.netease.result.LoginResultBean
import java.io.IOException

/**
 * author : yangbin10
 * date   : 2017/12/21
 */
@Route(path = "/netease/login")
internal class LoginActivity : BaseActivity() {

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
        inputPhone.onTextChanged {
            TransitionManager.beginDelayedTransition(container)
            tilPhone.isErrorEnabled = false
        }
        inputPassword.onTextChanged {
            TransitionManager.beginDelayedTransition(container)
            tilPassword.isErrorEnabled = false
        }
    }

    private fun EditText.onTextChanged(callback: () -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                callback()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
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
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
        jobLogin = launch(UI) {
            val phoneStr = inputPhone.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            if (!isValidPhone(phoneStr) || !isValidPassword(password)) {
                return@launch
            }
            progressDialog.show()
            val loginResult: LoginResultBean
            try {
                loginResult = NeteaseCloudMusicApi().login(phoneStr, password)
            } catch (e: IOException) {
                tilPhone.error = getString(R.string.netease_error_login_failed_template, e.message)
                throw CancellationException()
            }
            if (loginResult.code == 200) {
                loginSuccess(loginResult.profile!!)
            } else {
                tilPhone.error = getString(R.string.netease_error_login_failed_remote)
            }
            log { "login result $loginResult" }
        }.also {
            //bind progress dialog to login job.
            it.invokeOnCompletion {
                progressDialog.dismiss()
                jobLogin = null
            }
        }
    }

    private fun loginSuccess(profile: LoginResultBean.Profile) {
        NeteasePreference.saveLoginUser(profile)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.isEmpty()) {
            tilPassword.error = getString(R.string.netease_error_empty_password)
            return false
        }
        return true
    }

    private fun isValidPhone(phoneStr: String): Boolean {
        if (phoneStr.isEmpty()) {
            tilPhone.error = getString(R.string.netease_error_empty_phone)
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        jobLogin?.cancel()
    }
}