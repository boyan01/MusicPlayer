package tech.soit.quiet.utils.annotation

import androidx.annotation.LayoutRes

/**
 * order the fragment's layout id
 *
 * @param value the id of fragment layout
 * @param translucent  if false , it will set a background for this fragment' root view
 *                     if true , do nothing.
 */
@Target(AnnotationTarget.CLASS)
annotation class LayoutId(
        @LayoutRes val value: Int,
        val translucent: Boolean = true
)