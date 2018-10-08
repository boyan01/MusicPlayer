package tech.soit.quiet.utils.testing

import android.os.Bundle
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.view.ContentFrameLayout

/**
 * Used for testing fragments inside a fake activity.
 */
class SingleFragmentActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = ContentFrameLayout(this)
        content.id = R.id.content
        setContentView(content)
    }


    fun setFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.content, fragment, "test")
                .addToBackStack("test")
                .commitAllowingStateLoss()
    }

    fun replaceFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit()
    }


}