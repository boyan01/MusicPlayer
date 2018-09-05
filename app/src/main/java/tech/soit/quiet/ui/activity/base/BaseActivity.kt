package tech.soit.quiet.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.soit.quiet.R
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.annotation.LayoutId
import kotlin.reflect.full.findAnnotation

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isInjectLayout = this::class.findAnnotation<DisableLayoutInject>() == null
        val layoutId = this::class.findAnnotation<LayoutId>()
        if (isInjectLayout && layoutId != null) {
            setContentView(layoutId.value)
        }
    }

    /**
     * navigation to the fragment, if fragment exist, will not add
     *
     * NOTE: fragment' container has been ordered to [R.id.content]
     */
    open fun navigationTo(tag: String,
                          addToBackStack: Boolean = true,
                          fragment: () -> BaseFragment) {
        val exist = supportFragmentManager.findFragmentByTag(tag)
        if (exist != null && exist.isAdded) {
            supportFragmentManager.beginTransaction()
                    .show(exist)
                    .commit()
        } else {
            val new = fragment()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, new, tag)
                    .apply {
                        if (addToBackStack) {
                            addToBackStack(tag)
                        }
                    }
                    .commit()
        }

    }

}