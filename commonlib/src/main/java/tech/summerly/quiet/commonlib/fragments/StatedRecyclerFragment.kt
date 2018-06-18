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
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.log

/**
 * Created by summer on 18-2-27
 */
abstract class StatedRecyclerFragment<T> : BaseFragment() {

    protected var job: Job? = null

    protected open val isLoadOnCreated = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stated_recycler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        wrapper.setOnRetryButtonClick {
            loadDataInternal()
        }
        initRecyclerView(wrapper.recyclerView)
        if (isLoadOnCreated) {
            loadDataInternal()
        }
    }

    protected val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? get() = view?.wrapper?.recyclerView?.adapter

    protected open fun loadDataInternal() {
        if (job?.isActive == true) {//do not to load data when job is running.
            return
        }
        setLoading()
        job = asyncUI {
            try {
                val data = loadData()
                onLoadSuccess(data)
            } catch (e: Exception) {
                onLoadFailed(e)
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
    protected abstract suspend fun loadData(): List<T>


    protected open fun onLoadSuccess(result: List<T>) {
        setComplete()
    }

    protected open fun onLoadFailed(e: Exception) {
        log { e.printStackTrace() }
        setError(e.message)
    }

    fun setLoading() = runWithRoot {
        wrapper.setLoading()
    }

    fun setComplete() = runWithRoot {
        wrapper.setComplete()
    }

    fun setError(msg: String? = null) = runWithRoot {
        log { "search error : $msg" }
        wrapper.setError()
    }

    fun setEmpty() = runWithRoot {
        wrapper.setEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job = null
    }

}