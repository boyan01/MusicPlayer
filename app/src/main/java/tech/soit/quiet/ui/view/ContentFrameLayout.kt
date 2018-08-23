package tech.soit.quiet.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import tech.summerly.quiet.commonlib.utils.log


/**
 * ContentFrameLayout do not consume [WindowInsets]
 */
class ContentFrameLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        super.dispatchApplyWindowInsets(WindowInsets(insets))
        return insets
    }


    override fun setOnApplyWindowInsetsListener(listener: OnApplyWindowInsetsListener) {

    }

}