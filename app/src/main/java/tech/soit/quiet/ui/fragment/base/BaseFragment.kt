package tech.soit.quiet.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import tech.soit.quiet.utils.annotation.LayoutId
import kotlin.reflect.full.findAnnotation

/**
 * @see Fragment
 */
abstract class BaseFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = this::class.findAnnotation<LayoutId>()
        return if (layoutId == null) {
            super.onCreateView(inflater, container, savedInstanceState)
        } else {
            inflater.inflate(layoutId.value, container, false)
        }
    }


    /**
     * lazy generate ViewModel
     *
     * example :
     *   private val viewModel by lazyViewModel(XXXViewModel::class)
     *
     */
    protected inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> = lazy {
        ViewModelProviders.of(requireActivity()).get(T::class.java)
    }


}