package tech.soit.quiet.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.util.ObjectsCompat
import tech.soit.quiet.R
import tech.soit.quiet.utils.component.support.attrValue


/**
 * ContentFrameLayout do not consume [WindowInsets]
 */
class ContentFrameLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {


    var statusBarBackground: Drawable = ColorDrawable(context.attrValue(R.attr.colorPrimaryDark))

    private var mDrawStatusBarBackground: Boolean = false

    private var mLastInsets: WindowInsets? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDrawStatusBarBackground) {
            val inset = mLastInsets?.systemWindowInsetTop ?: 0
            if (inset > 0) {
                statusBarBackground.setBounds(0, 0, width, inset)
                statusBarBackground.draw(canvas)
            }
        }
    }


    /**
     * not consume [WindowInsets]
     */
    override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
        if (fitsSystemWindows && !ObjectsCompat.equals(mLastInsets, insets)) {
            mLastInsets = insets
            mDrawStatusBarBackground = insets.systemWindowInsetTop > 0
            setWillNotDraw(!mDrawStatusBarBackground && background == null)
            requestLayout()
        }
        super.dispatchApplyWindowInsets(WindowInsets(insets))
        return insets
    }

}