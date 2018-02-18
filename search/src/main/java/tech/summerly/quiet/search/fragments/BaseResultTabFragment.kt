package tech.summerly.quiet.search.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_fragment_result_tab.view.*
import kotlinx.coroutines.experimental.Job
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.utils.*
import tech.summerly.quiet.search.R
import java.io.IOException

/**
 * Created by summer on 18-2-18
 */
internal abstract class BaseResultTabFragment : BaseFragment() {

    companion object {

        const val KEY_QUERY_TEXT = SearchResultsFragment.KEY_QUERY_TEXT

    }

    private val items = ArrayList<Any>()

    private var job: Job? = null

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
        recycler.adapter = MultiTypeAdapter(items).also {

        }
        startQueryAsync()
    }

    /**
     * start an async task to query from service
     */
    private fun startQueryAsync() {
        val text = arguments?.getString(KEY_QUERY_TEXT)?.trim()
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

    abstract suspend fun startQuery(text: String)

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
        progressBar.gone()
        imageError.visible()
        buttonRetry.visible()
    }

    protected fun showItems(items: List<Any>) = runWithRoot {

        log { "$items" }
//        recycler.multiTypeAdapter.setItemsByDiff(items)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}