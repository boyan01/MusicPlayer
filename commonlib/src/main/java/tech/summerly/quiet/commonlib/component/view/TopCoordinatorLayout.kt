package tech.summerly.quiet.commonlib.component.view

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.WindowInsets

class TopCoordinatorLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val copy = WindowInsets(insets)
        super.dispatchApplyWindowInsets(copy)
        return insets
    }

}