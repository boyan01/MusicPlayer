package tech.summerly.quiet.search.fragments.result

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_content_result_tabs.view.*
import kotlinx.android.synthetic.main.search_fragment_results.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.fragments.UnimplementedFragment
import tech.summerly.quiet.search.R

/**
 * Created by summer on 18-2-18
 */
internal class SearchResultsFragment : BaseFragment() {

    companion object {

        @Deprecated("...")
        fun newInstance(query: String): SearchResultsFragment {
            return SearchResultsFragment()
        }

        private const val COUNT_PAGE = 3
    }

    private var _query: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment_results, container, false)
    }

    private val pagerAdapter by lazy {
        SectionsPagerAdapter(childFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
        pager.offscreenPageLimit = COUNT_PAGE
    }

    /**
     * search [query] and display the result to screen
     */
    internal fun search(query: String) {
        if (_query == query) {
            return
        }
        _query = query

        if (host == null) {
            return
        }
        // hang out the query event to all child fragment
        pagerAdapter.fragments.forEach {
            it.search(query)
        }
    }

    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val _fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = _fragments[position] ?: when (position) {
            0 -> MusicsResultTabFragment()
            1 -> UnimplementedFragment()
            2 -> UnimplementedFragment()
            else -> UnimplementedFragment()
        }.also {
            if (it is BaseResultTabFragment) {
                it.search(_query)
            }
            _fragments[position] = it
        }

        val fragments get() = _fragments.filterIsInstance(BaseResultTabFragment::class.java)


        // display four fragment: overview , total , artist , album
        override fun getCount(): Int = COUNT_PAGE

    }

}