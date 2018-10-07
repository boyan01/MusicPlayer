package tech.soit.quiet.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main_cloud.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.fragment.home.cloud.getCloudNavigators
import tech.soit.quiet.ui.fragment.home.cloud.withCloudNavigators
import tech.soit.quiet.utils.annotation.LayoutId

@LayoutId(R.layout.fragment_main_cloud)
class MainCloudFragment : BaseFragment() {


    private lateinit var adapter: MultiTypeAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init adapters
        adapter = MultiTypeAdapter().withCloudNavigators()
        recyclerView.adapter = adapter
        (recyclerView.layoutManager as GridLayoutManager).apply {
            spanCount = 3
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return 1
                }
            }
        }

        setupFastUpArrow()

        //load data
        adapter.items = getCloudNavigators()
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

}