package tech.summerly.quiet.search

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.search_activity_main.*
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.constraints.Search
import tech.summerly.quiet.search.fragments.SearchDefaultFragment
import tech.summerly.quiet.search.fragments.SearchResultsFragment
import tech.summerly.quiet.search.fragments.items.History
import tech.summerly.quiet.search.utils.getHistory
import tech.summerly.quiet.search.utils.inTransaction
import tech.summerly.quiet.search.utils.saveHistory

/**
 * Created by summer on 18-2-17
 */
@Route(path = Search.ACTIVITY_SEARCH_MAIN)
class SearchMainActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity_main)
        initView()
    }

    private fun initView() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                startQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.isIconified = false
        supportFragmentManager.inTransaction {
            replace(R.id.layoutContainer, SearchDefaultFragment())
        }
    }

    fun startQuery(text: String = searchView.query.toString().trim()) {
        log { "查询:$text" }
        if (text.isEmpty()) {
            //do nothing
            return
        }
        if (text != searchView.query.toString()) {
            searchView.setQuery(text, false)
        }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
        val fragment = SearchResultsFragment.newInstance(text)
        supportFragmentManager.inTransaction {
            replace(R.id.layoutContainer, fragment)
        }
        launch {
            val histories = getHistory().toMutableList()
            val history = histories.find { it.text == text }
            if (history != null) {
                histories.remove(history)
            }
            histories.add(0, History(text, System.currentTimeMillis()))
            saveHistory(histories)
        }
    }
}