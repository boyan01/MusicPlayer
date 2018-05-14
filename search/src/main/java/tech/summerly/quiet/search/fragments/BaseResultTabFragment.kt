package tech.summerly.quiet.search.fragments

import android.support.v7.widget.RecyclerView
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.objects.PortionList
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.search.adapters.SearchResultAdapter
import tech.summerly.quiet.search.model.SearchInterface
import tech.summerly.quiet.search.repository.NeteaseRepository
import tech.summerly.quiet.search.utils.LoadMoreDelegate

/**
 * Created by summer on 18-2-18
 */
internal abstract class BaseResultTabFragment : StatedRecyclerFragment<Any>(), LoadMoreDelegate.OnLoadMoreListener {


    companion object {
        const val KEY_QUERY_TEXT = SearchResultsFragment.KEY_QUERY_TEXT
    }

    override val canLoadMore: Boolean
        get() = !isLoading && !isCompleted


    private val items = PortionList(ArrayList(), 0, 0)

    //is loading data from server
    private val isLoading: Boolean get() = job != null

    //has been load all portion of search result
    private val isCompleted: Boolean get() = items.size >= items.total

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SearchResultAdapter(items)
        LoadMoreDelegate(this@BaseResultTabFragment).attach(recyclerView)
    }

    private val keyword get() = arguments?.getString(KEY_QUERY_TEXT) ?: ""

    override suspend fun loadData(): List<Any> {
        return startQuery(keyword, items.size)
    }

    protected fun search(): SearchInterface = NeteaseRepository()

    override fun loadMore() {
        loadDataInternal()
    }

    abstract suspend fun startQuery(text: String, offset: Int): List<Any>

    override fun onLoadSuccess(result: List<Any>) {
        super.onLoadSuccess(result)
        if (result is PortionList && result.offset == items.size) {
            items.mix(result)
        } else {
            log(LoggerLevel.ERROR) { "illegal argument : ${result::class}" }
        }
        adapter?.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}