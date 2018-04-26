@file:Suppress("MemberVisibilityCanBePrivate")

package tech.summerly.quiet.commonlib.fragments

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_stated_recycler.view.*
import kotlinx.coroutines.experimental.Job
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.utils.*

/**
 * Created by summer on 18-2-27
 */
abstract class StatedRecyclerFragment : BaseFragment() {

    protected var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stated_recycler, container, false)
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        buttonRetry.setOnClickListenerSafely {
            if (job?.isActive == true) {
                log { "job is active , but retry button has been clicked" }
                job?.cancel()
            }
            loadDataInternal()
        }
        initRecyclerView(recycler)
        loadDataInternal()
    }

    protected val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? get() = view?.recycler?.adapter

    private fun loadDataInternal() {
        if (job?.isActive == true) {//do not to load data when job is running.
            return
        }
        setLoading()
        job = asyncUI {
            try {
                loadData()
                adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                setError(e.message)
            }
        }
    }

    protected abstract fun initRecyclerView(recyclerView: RecyclerView)

    /**
     * 在这里加载 recycler view adapter [adapter] 所需要的数据
     *
     * 需自己手动调用 [setComplete] 来清楚 loading 状态
     */
    @UiThread
    protected abstract suspend fun loadData()

    fun setLoading() = runWithRoot {
        progressBar.visible()
        buttonRetry.gone()
        textEmptyDescription.gone()
    }

    fun setComplete() = runWithRoot {
        progressBar.gone()
        buttonRetry.gone()
        textEmptyDescription.gone()
    }

    fun setError(msg: String? = null) = runWithRoot {
        log { "search error : $msg" }
        progressBar.gone()
        buttonRetry.visible()
        textEmptyDescription.gone()
    }

    fun setEmpty() = runWithRoot {
        progressBar.gone()
        buttonRetry.gone()
        textEmptyDescription.visible()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job = null
    }

}