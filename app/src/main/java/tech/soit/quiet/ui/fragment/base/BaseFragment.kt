package tech.soit.quiet.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import tech.soit.quiet.ui.view.ContentFrameLayout
import tech.soit.quiet.utils.annotation.DisableLayoutInject
import tech.soit.quiet.utils.annotation.LayoutId
import kotlin.reflect.full.findAnnotation

/**
 * @see Fragment
 */
abstract class BaseFragment : Fragment() {

    lateinit var viewModelFactory: ViewModelProvider.Factory

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


}