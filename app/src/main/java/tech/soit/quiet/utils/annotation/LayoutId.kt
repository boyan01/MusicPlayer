package tech.soit.quiet.utils.annotation

import androidx.annotation.LayoutRes
import tech.soit.quiet.ui.fragment.base.BaseFragment
import java.lang.annotation.Inherited

/**
 * bind the fragment's layout id to fragment by annotation
 * this binder process will be invoke in [BaseFragment.onCreateView]
 *
 * this annotation will be ignore, if your override [BaseFragment.onCreateView2] and return non null
 *
 * @param value the id of fragment layout
 * @param translucent  if false , it will set a background for this fragment' root view
 *                     if true , do nothing.
 */
@Target(AnnotationTarget.CLASS)
@Inherited
annotation class LayoutId(
        @LayoutRes val value: Int,
        val translucent: Boolean = true
)