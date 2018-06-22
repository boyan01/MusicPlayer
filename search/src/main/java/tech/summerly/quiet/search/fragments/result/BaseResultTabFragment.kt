package tech.summerly.quiet.search.fragments.result

import android.support.v7.widget.RecyclerView
import tech.summerly.quiet.commonlib.fragments.StatedRecyclerFragment
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.objects.PortionList
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.support.RemoteTypedBinderWrapper
import tech.summerly.quiet.commonlib.utils.support.TypedAdapter
import tech.summerly.quiet.constraints.PlaylistDetail
import tech.summerly.quiet.search.model.SearchInterface
import tech.summerly.quiet.search.model.SearchResult
import tech.summerly.quiet.search.repository.NeteaseRepository
import tech.summerly.quiet.search.utils.LoadMoreDelegate

/**
 * Created by summer on 18-2-18
 */
internal abstract class BaseResultTabFragment : StatedRecyclerFragment<Any>(),
        LoadMoreDelegate.OnLoadMoreListener {


    private val _adapter by lazy {
        val musicBinder = RemoteTypedBinderWrapper.withPath<IMusic>(PlaylistDetail.ITEM_BINDER_MUSIC)
        musicBinder.invoke("withOnItemClickListener", onMusicClick)
        TypedAdapter()
                .withBinder(SearchResult.Music::class,
                        musicBinder) { it.music }
                .withBinder(SearchResult.Artist::class,
                        RemoteTypedBinderWrapper.withPath("/items/artist")) { it.artist }
                .withBinder(SearchResult.Album::class,
                        RemoteTypedBinderWrapper.withPath("/items/album")) { it.album }
    }

    private val onMusicClick: (music: IMusic) -> Unit = {
        MusicPlayerManager.play(it)
    }

    override val isLoadOnCreated: Boolean
        get() = false

    override val canLoadMore: Boolean
        get() = !isLoading && !isCompleted

    //restored query keyword
    private var _query: String? = null


    private val items = PortionList(ArrayList(), 0, 0)

    //is loading data from server
    private val isLoading: Boolean get() = job != null

    //has been load all portion of search result
    private val isCompleted: Boolean get() = items.size >= items.total

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = _adapter
        LoadMoreDelegate(this@BaseResultTabFragment).attach(recyclerView)
    }

    override suspend fun loadData(): List<Any> {
        val query = _query ?: return emptyList()
        return queryInternal(query, items.size)
    }

    protected fun search(): SearchInterface = NeteaseRepository()

    /**
     * start to search a new keyword
     */
    internal fun search(query: String) {
        if (_query == query) {
            return
        }
        _query = query
        items.clear()
        loadDataInternal()
    }


    abstract suspend fun queryInternal(query: String, offset: Int): List<SearchResult>

    override fun loadMore() {
        loadDataInternal()
    }


    override fun onLoadSuccess(result: List<Any>) {
        super.onLoadSuccess(result)
        if (result is PortionList && result.offset == items.size) {
            items.mix(result)
        } else {
            log(LoggerLevel.ERROR) { "illegal argument : ${result::class}" }
        }
        _adapter.submit(items)
    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}