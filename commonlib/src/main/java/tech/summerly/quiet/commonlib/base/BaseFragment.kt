package tech.summerly.quiet.commonlib.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by summer on 17-12-17
 * as backup
 */
open class BaseFragment : Fragment() {

    private var rootView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
    }

    /**
     * run if root view is not null
     */
    protected fun runWithRoot(run: View.() -> Unit) {
        rootView?.run()
    }
}