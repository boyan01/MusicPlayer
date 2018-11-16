package tech.soit.quiet.ui.activity.cloud

import android.os.Bundle
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_cloud_top_detail.*
import kotlinx.coroutines.launch
import tech.soit.quiet.R
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.adapter.TopDetailListAdapter
import tech.soit.quiet.ui.activity.cloud.adapter.TopDetailListAdapter.Companion.INDEX_GLOBAL
import tech.soit.quiet.ui.activity.cloud.adapter.TopDetailListAdapter.Companion.INDEX_OFFICIAL
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log


@LayoutId(R.layout.activity_cloud_top_detail)
@EnableBottomController
class TopDetailActivity : BaseActivity() {

    private val neteaseRepository by lazyViewModel<NeteaseRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRoot.setOnApplyWindowInsetsListener { _, insets ->
            toolbar.updatePadding(top = insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return if (position in INDEX_OFFICIAL..INDEX_GLOBAL) 3 else 1
            }

        }
        recyclerView.layoutManager = gridLayoutManager
        launch {
            try {
                val detail = neteaseRepository.toplistDetail()
                recyclerView.adapter = TopDetailListAdapter(detail)
            } catch (e: Exception) {
                log { e.printStackTrace();"error" }
            }
        }
    }


}