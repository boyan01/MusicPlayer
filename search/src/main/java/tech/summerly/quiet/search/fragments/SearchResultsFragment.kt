package tech.summerly.quiet.search.fragments

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

        const val KEY_QUERY_TEXT = "query_text"

        fun newInstance(query: String): SearchResultsFragment {
            return SearchResultsFragment().also {
                val bundle = Bundle()
                bundle.putString(KEY_QUERY_TEXT, query)
                it.arguments = bundle
            }
        }

        private const val COUNT_PAGE = 3
    }

    private val query: String get() = arguments?.getString(KEY_QUERY_TEXT) ?: ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        pager.adapter = SectionsPagerAdapter(childFragmentManager)
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
        pager.offscreenPageLimit = COUNT_PAGE
    }

    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        val fragments = Array<Fragment?>(count) { null }

        override fun getItem(position: Int): Fragment? = fragments[position] ?: when (position) {
            0 -> MusicsResultTabFragment.newInstance(query)
            1 -> UnimplementedFragment()
            2 -> UnimplementedFragment()
            else -> UnimplementedFragment()
        }.also {
            fragments[position] = it
        }


        // display four fragment: overview , total , artist , album
        override fun getCount(): Int = COUNT_PAGE

    }

}