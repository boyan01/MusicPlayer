package tech.soit.quiet.ui.activity.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_login.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.main.AppMainActivity
import tech.soit.quiet.ui.activity.user.viewmodel.LoginViewModel
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.QuietViewModelProvider
import tech.soit.quiet.utils.component.support.setOnClickListenerAsync

/**
 * 暂时先这样吧，登陆界面能跑就行了
 */
@LayoutId(R.layout.activity_login)
class LoginActivity : BaseActivity() {

    init {
        viewModelFactory = object : QuietViewModelProvider() {
            override fun createViewModel(modelClass: Class<ViewModel>): ViewModel {
                if (modelClass == LoginViewModel::class.java) {
                    return LoginViewModel()
                }
                return super.createViewModel(modelClass)
            }
        }
    }

    private val loginViewModel by lazyViewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonLogin.setOnClickListenerAsync {
            val phone = editPhone.text?.toString()
            val password = editPassword.text?.toString()
            val (isSuccess, msg) = loginViewModel.login(phone, password)
            if (isSuccess) {
                startActivity(Intent(this@LoginActivity, AppMainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@LoginActivity, msg!!, Toast.LENGTH_SHORT).show()
            }
        }
    }

}