package tech.summerly.quiet.commonlib.component.view

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

/**
 * 此 View 监听 WindowInsets 的分发并作出相应的变换，但是不消耗 WindowInsets
 */
class FitSystemFrameLayout @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        super.dispatchApplyWindowInsets(WindowInsets(insets))
        return insets
    }

}