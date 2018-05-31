package tech.summerly.quiet.commonlib.component.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.WindowInsetsCompat
import android.util.AttributeSet
import android.widget.FrameLayout

class FitSystemFrameLayout @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        fitsSystemWindows = true
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            //consume a copy of insets,so that this view do no intercept the WindowInsets
            ViewCompat.onApplyWindowInsets(view, WindowInsetsCompat(insets))
            insets
        }
    }

}