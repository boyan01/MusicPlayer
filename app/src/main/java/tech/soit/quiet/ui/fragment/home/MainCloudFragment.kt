package tech.soit.quiet.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main_cloud.*
import kotlinx.coroutines.launch
import tech.soit.quiet.R
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.ui.adapter.CloudMainAdapter
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log

@LayoutId(R.layout.fragment_main_cloud)
class MainCloudFragment : BaseFragment() {


    private lateinit var adapter: CloudMainAdapter

    private val neteaseRepository by lazyViewModel<NeteaseRepository>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init adapters
        adapter = CloudMainAdapter()
        recyclerView.adapter = adapter
        (recyclerView.layoutManager as GridLayoutManager).apply {
            spanCount = CloudMainAdapter.SPAN_COUNT
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val binder = adapter.typePool.getItemViewBinder(adapter.getItemViewType(position))
                    binder as KItemViewBinder
                    return binder.spanSize
                }
            }
        }

        setupFastUpArrow()
        adapter.refresh()

        loadData()

    }

    /**
     * setup fast up arrow which can fast scroll to top
     */
    private fun setupFastUpArrow() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrolled = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                scrolled += dy
                if (scrolled > (recyclerView.height / 2)) {
                    fabUp.show()
                } else {
                    fabUp.hide()
                }
            }
        })
        fabUp.hide()//hide first, because RecyclerView' scrolled dy must be 0 currently
        fabUp.setOnClickListener {
            recyclerView.scrollToPosition(0)
        }
    }

    private fun loadData() {

        launch {

            try {
                val playlist = neteaseRepository.personalizedPlaylist(limit = 6)
                adapter.setRecommendPlaylist(playlist)
            } catch (e: Exception) {
                log { e.printStackTrace();"出错" }
            }
        }

    }

}