package tech.soit.quiet.utils.annotation

import androidx.annotation.LayoutRes

/**
 * order the fragment's layout id
 */
@Target(AnnotationTarget.CLASS)
annotation class LayoutId(
        @LayoutRes val value: Int
)