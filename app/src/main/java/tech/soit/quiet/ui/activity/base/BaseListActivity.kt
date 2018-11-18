package tech.soit.quiet.ui.activity.base

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import kotlinx.android.synthetic.main.activity_base_list.*
import tech.soit.quiet.R
import tech.soit.quiet.utils.annotation.LayoutId


/**
 * the base activity which holder a recycler view
 *
 */
@LayoutId(R.layout.activity_base_list)
abstract class BaseListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //toolbar setup
        layoutRoot.setOnApplyWindowInsetsListener { _, insets ->
            toolbar.updatePadding(top = toolbar.paddingTop + insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        buttonRetry.setOnClickListener { onRetryButtonClicked() }
    }


    /**
     * 显示载入动画
     */
    protected open fun setLoading() {
        recyclerView.isVisible = false
        progressBar.isVisible = true
        buttonRetry.isVisible = false
    }

    /**
     * 失败
     */
    protected open fun setFailed() {
        recyclerView.isVisible = false
        progressBar.isVisible = false
        buttonRetry.isVisible = true
    }

    /**
     * load success
     */
    protected open fun setSuccess() {
        recyclerView.isVisible = true
        progressBar.isVisible = false
        buttonRetry.isVisible = false
    }

    /**
     * 重试按钮的点击回调
     */
    protected open fun onRetryButtonClicked() {
        setLoading()
    }


}