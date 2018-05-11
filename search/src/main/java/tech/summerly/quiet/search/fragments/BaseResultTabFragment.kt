package tech.summerly.quiet.search.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_fragment_result_tab.view.*
import kotlinx.coroutines.experimental.Job
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.objects.PortionList
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.search.R
import tech.summerly.quiet.search.adapters.SearchResultAdapter
import tech.summerly.quiet.search.model.SearchInterface
import tech.summerly.quiet.search.repository.NeteaseRepository
import tech.summerly.quiet.search.utils.LoadMoreDelegate
import java.io.IOException

/**
 * Created by summer on 18-2-18
 */
internal abstract class BaseResultTabFragment : BaseFragment(), LoadMoreDelegate.OnLoadMoreListener {


    companion object {
        const val KEY_QUERY_TEXT = SearchResultsFragment.KEY_QUERY_TEXT
    }

    override val canLoadMore: Boolean
        get() = !isLoading && !isCompleted


    private val items = PortionList(ArrayList(), 0, 0)

    private var job: Job? = null

    //is loading data from server
    private val isLoading: Boolean get() = job != null

    //has been load all portion of search result
    private val isCompleted: Boolean get() = items.size >= items.total

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment_result_tab, container, false)
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        buttonRetry.setOnClickListener {
            if (job != null) {
                log(LoggerLevel.ERROR) { "search job is not complete, but retry button has been clicked" }
                job?.cancel()
            }
            startQueryAsync()
        }
        recycler.adapter = SearchResultAdapter(items)
        startQueryAsync()
        LoadMoreDelegate(this@BaseResultTabFragment).attach(recycler)
    }

    private fun onMusicClick(music: Music) {
        log { "$music has been clicked" }
        MusicPlayerManager.musicPlayer(MusicType.NETEASE).play(music)
    }

    protected fun search(): SearchInterface = NeteaseRepository()

    /**
     * start an async task to query from service
     */
    private fun startQueryAsync() {
        val text = arguments?.getString(KEY_QUERY_TEXT)?.trim()

        if (isLoading) {
            return
        }
        //first to check text
        if (text == null || text.isEmpty()) {
            setError()
        } else {
            setLoading()
            job = asyncUI {
                try {
                    startQuery(text)
                    setComplete()
                } catch (e: IOException) {
                    e.printStackTrace()
                    setError(e.localizedMessage)
                }
            }.also { it.invokeOnCompletion { job = null } }
        }
    }

    override fun loadMore() {
        startQueryAsync()
    }

    abstract suspend fun startQuery(text: String, offset: Int = items.size)

    private fun setLoading() = runWithRoot {
        progressBar.visible()
        imageError.gone()
        buttonRetry.gone()
    }

    private fun setComplete() = runWithRoot {
        progressBar.gone()
        imageError.gone()
        buttonRetry.gone()
    }

    private fun setError(msg: String? = null) = runWithRoot {
        log { "search error : $msg" }
        progressBar.gone()
        imageError.visible()
        buttonRetry.visible()
    }

    protected fun showItems(newPortion: PortionList<*>) = runWithRoot {
        //first to check portion list legality
        if (newPortion.offset != items.size) {
            return@runWithRoot
        }
        items.mix(newPortion)
        recycler.multiTypeAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}