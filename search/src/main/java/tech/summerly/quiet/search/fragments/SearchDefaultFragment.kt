package tech.summerly.quiet.search.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_fragment_default.view.*
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.search.R
import tech.summerly.quiet.search.SearchMainActivity
import tech.summerly.quiet.search.fragments.items.History
import tech.summerly.quiet.search.fragments.items.SearchHistoryViewBinder
import tech.summerly.quiet.search.fragments.items.SearchHotHint
import tech.summerly.quiet.search.fragments.items.SearchHotHintViewBinder
import tech.summerly.quiet.search.utils.getHistory
import tech.summerly.quiet.search.utils.saveHistory

/**
 * Created by summer on 18-3-6
 *
 * [SearchMainActivity] 默认显示的fragment
 *
 * 目前只包含搜索历史记录
 */
internal class SearchDefaultFragment : BaseFragment() {

    private val items = Items(15)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment_default, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = MultiTypeAdapter(items)
        recyclerView.multiTypeAdapter.apply {
            register(History::class.java, SearchHistoryViewBinder(historyClick, historyRemove))
            register(SearchHotHint::class.java, SearchHotHintViewBinder(search))
        }
        Unit
    }

    override fun onResume() {
        super.onResume()
        asyncUI {
            items.clear()
            items.add(SearchHotHint(listOf("慢慢喜欢你", "等你下课", "五月天", "方大同", "9420", "Welcome To New York")))
            items.addAll(getHistory())
            log { " $items" }
            view?.recyclerView?.multiTypeAdapter?.notifyDataSetChanged()
        }
    }

    private val search = fun(query: String) {
        val searchMainActivity = activity as? SearchMainActivity ?: return
        searchMainActivity.startQuery(query)
    }

    private val historyClick = fun(history: History) {
        search(history.text)
    }

    private val historyRemove = fun(history: History, position: Int) = runWithRoot {
        items.remove(history)
        recyclerView.multiTypeAdapter.notifyItemRemoved(position)
        asyncUI {
            saveHistory(items.filterIsInstance(History::class.java))
        }
    }
}