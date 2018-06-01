package tech.summerly.quiet.commonlib.component.view

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.forEach

/**
 * 此 View 监听 WindowInsets 的分发并作出相应的变换，但是不消耗 WindowInsets
 *
 * 注意：子View会被自动设置fitSystemWindows=true
 *
 */
class FitSystemFrameLayout @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {


    /**
     * this layout do not consume WindowsInsets ,
     * and dispatch a copy of WindowInset to child
     */
    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val copy = WindowInsets(insets)
        forEach {
            it.fitsSystemWindows = true
            it.dispatchApplyWindowInsets(copy)
            if (copy.isConsumed) {
                return@forEach
            }
        }
        return insets
    }

}