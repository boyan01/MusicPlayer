package tech.soit.quiet.ui.fragment.base

import android.os.Build
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
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.view.ContentFrameLayout
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.QuietViewModelProvider
import tech.soit.quiet.utils.component.support.attrValue
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.reflect.full.findAnnotation

/**
 * @see Fragment
 */
abstract class BaseFragment : Fragment(), CoroutineScope {

    var viewModelFactory: ViewModelProvider.Factory = QuietViewModelProvider()

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = onCreateView2(inflater, container, savedInstanceState)
        if (view == null) {
            view = getAnnotatedLayout(inflater, container)
        }
        if (view != null && view !is ContentFrameLayout) {
            val wrapper = ContentFrameLayout(inflater.context)
            wrapper.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            wrapper.addView(view)
            return wrapper
        }
        return view
    }

    /**
     * inflate view by [LayoutId] annotation
     *  if annotation is not presenter , null returned
     */
    protected fun getAnnotatedLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        val layoutId = this::class.findAnnotation<LayoutId>() ?: return null
        val view = inflater.inflate(layoutId.value, container, false)
        if (!layoutId.translucent) {
            view.setBackgroundColor(view.context.attrValue(R.attr.quietBackground))
        }
        return view
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


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    open fun onBackPressed() {
        val isStateSaved = childFragmentManager.isStateSaved
        if (isStateSaved && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            // Older versions will throw an exception from the framework
            // FragmentManager.popBackStackImmediate(), so we'll just
            // return here. The Activity is likely already on its way out
            // since the fragmentManager has already been saved.
            return
        }

        if (isStateSaved || !childFragmentManager.popBackStackImmediate()) {
            requireBaseActivity().onBackPressed()
        }
    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


}