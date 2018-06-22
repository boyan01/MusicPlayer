package tech.summerly.quiet.search.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.search_fragment_main.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.component.callback.BottomControllerHost
import tech.summerly.quiet.constraints.Search
import tech.summerly.quiet.search.R
import tech.summerly.quiet.search.fragments.result.SearchResultsFragment

/**
 * FRAGMENT_SEARCH_MAIN
 */
@Route(path = Search.FRAGMENT_SEARCH_MAIN)
class SearchMainFragment : BaseFragment(), BottomControllerHost {

    companion object {

        private const val TAG_SEARCH_RESULT_FRAGMENT = "search_result"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        buttonBack.setOnClickListener {
            closeSelf()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.isIconified = false
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .replace(R.id.resultContent, SearchDefaultFragment())
                    .commit()
        }
    }


    fun search(query: String) {
        val q = query.trim()
        if (q.isEmpty()) {
            return
        }
        view?.searchView?.clearFocus()
        searchInternal(query)
        //TODO record search history
    }

    override fun onStop() {
        super.onStop()
        view?.searchView?.clearFocus()
    }

    private fun searchInternal(query: String) {
        var resultF = childFragmentManager.findFragmentByTag(TAG_SEARCH_RESULT_FRAGMENT) as? SearchResultsFragment
        if (resultF == null) {
            resultF = SearchResultsFragment()
        }
        if (resultF.host == null) {
            childFragmentManager.beginTransaction()
                    .replace(R.id.resultContent, resultF, TAG_SEARCH_RESULT_FRAGMENT)
                    .commit()
        }
        resultF.search(query)
    }

}