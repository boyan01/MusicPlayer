package tech.summerly.quiet.search.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


fun FragmentManager.inTransaction(transaction: FragmentTransaction.() -> Unit) {
    val t = beginTransaction()
    try {
        t.transaction()
    } finally {
        t.commit()
    }
}


class LoadMoreDelegate(private val onLoadMoreListener: OnLoadMoreListener) {


    fun attach(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(LoadMoreListener(onLoadMoreListener))
    }

    class LoadMoreListener(
            private val onLoadMoreListener: OnLoadMoreListener) : RecyclerView.OnScrollListener() {
        companion object {
            private const val VISIBLE_THRESHOLD = 4
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (!onLoadMoreListener.canLoadMore) {
                return
            }
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val itemCount = layoutManager.itemCount
            val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
            val isBottom = VISIBLE_THRESHOLD > itemCount - lastVisiblePosition
            if (isBottom) {
                onLoadMoreListener.loadMore()
            }
        }
    }

    interface OnLoadMoreListener {

        /**
         * to check current is need to load more data
         */
        val canLoadMore: Boolean

        /**
         * to load more data
         */
        fun loadMore()
    }
}
