package tech.summerly.quiet.commonlib.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import androidx.core.content.systemService
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.reflect.KProperty

/**
 * Created by summer on 17-12-17
 * as backup
 */
open class BaseFragment : Fragment() {

    private var rootView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
        //让 fragment 布局也支持 fitSystemWindows 属性
        (activity as? BaseActivity)?.requestApplyInserts()
    }

    /**
     * run if root view is not null
     */
    protected fun runWithRoot(run: View.() -> Unit) {
        rootView?.run()
    }

    protected open fun closeSelf() {
        fragmentManager?.apply {
            val transaction = beginTransaction()
            transaction.remove(this@BaseFragment)
            transaction.commit()
            popBackStack()
        }
    }

    protected fun withRoot(run: suspend View.() -> Unit) {
        launch(UI) { rootView?.run() }
    }

    protected operator fun <T> getValue(thisRef: Any?, kProperty: KProperty<*>): T {
        return getArgument(kProperty.name)
    }

    protected fun <T> getArgument(key: String): T {
        val bundle = arguments!!
        @Suppress("UNCHECKED_CAST")
        return bundle.get(key) as T
    }


    protected inline fun <reified T> doWithSystemService(run: T.() -> Unit) {
        val service = context?.systemService<T>() ?: return
        service.run()
    }

}