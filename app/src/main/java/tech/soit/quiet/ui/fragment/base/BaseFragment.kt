package tech.soit.quiet.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import tech.soit.quiet.AppContext
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.view.ContentFrameLayout
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.annotation.LayoutId
import kotlin.reflect.full.findAnnotation

/**
 * @see Fragment
 */
abstract class BaseFragment : Fragment() {

    var viewModelFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory(AppContext)

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = this::class.findAnnotation<LayoutId>()
        val isInjectLayout = this::class.findAnnotation<DisableLayoutInject>() == null
        val view = if (!isInjectLayout || layoutId == null) {
            onCreateView2(inflater, container, savedInstanceState)
        } else {
            inflater.inflate(layoutId.value, container, false)
        }
        view ?: return null
        val content = ContentFrameLayout(inflater.context)
        content.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        content.addView(view)
        return content
    }

    /**
     * @see Fragment.onCreateView
     *
     * @return Return the View for the fragment's UI, or null.
     */
    open fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.requestApplyInsets()
    }


    /**
     * lazy generate ViewModel
     *
     * example :
     *   private val viewModel by lazyViewModel(XXXViewModel::class)
     *
     */
    protected inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> = lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(T::class.java)
    }


    /**
     * lazy generate ViewModel , this ViewModel store is use the fragment
     *
     * example :
     *   private val viewModel by lazyViewModel(XXXViewModel::class)
     *
     */
    protected inline fun <reified T : ViewModel> lazyViewModelInternal(): Lazy<T> = lazy {
        ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
    }

    /**
     * cast the activity to [BaseActivity]
     */
    protected fun requireBaseActivity() = requireActivity() as BaseActivity


    /**
     * close this fragment from host
     */
    fun close() {
        if (!isAdded) {
            return
        }
        requireFragmentManager().beginTransaction()
                .remove(this)
                .commit()
    }

}