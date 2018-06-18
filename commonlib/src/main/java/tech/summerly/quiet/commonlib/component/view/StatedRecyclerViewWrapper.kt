package tech.summerly.quiet.commonlib.component.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_stated_recycler_wrapper.view.*
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.utils.gone
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.visible

open class StatedRecyclerViewWrapper @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        @Suppress("LeakingThis")
        View.inflate(context, R.layout.view_stated_recycler_wrapper, this)
        setEmpty()
    }


    val recyclerView: RecyclerView get() = recycler

    fun setOnRetryButtonClick(listener: (View.() -> Unit)?) {
        buttonRetry.setOnClickListener(listener)
    }

    fun setLoading() {
        progressBar.visible()
        buttonRetry.gone()
        textEmptyDescription.gone()
    }

    fun setComplete() {
        progressBar.gone()
        buttonRetry.gone()
        textEmptyDescription.gone()
    }

    fun setError(msg: String? = null) {
        log { "search error : $msg" }
        progressBar.gone()
        buttonRetry.visible()
        textEmptyDescription.gone()
    }

    fun setEmpty() {
        progressBar.gone()
        buttonRetry.gone()
        textEmptyDescription.visible()
    }

}