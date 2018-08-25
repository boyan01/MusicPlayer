package tech.soit.quiet.utils.testing

import android.os.Bundle
import android.view.View
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.base.BaseFragment

/**
 * Used for testing fragments inside a fake activity.
 */
class SingleFragmentActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))
    }


    fun setFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.content, fragment, "test")
                .commit()
    }

    fun replaceFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit()
    }


}