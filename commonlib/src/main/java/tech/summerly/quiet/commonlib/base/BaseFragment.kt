package tech.summerly.quiet.commonlib.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by summer on 17-12-17
 * as backup
 */
open class BaseFragment : Fragment() {

    @Suppress("RedundantOverride")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * run if root view is not null
     */
    protected fun runWithRoot(run: View.() -> Unit) {
        view?.run()
    }

}