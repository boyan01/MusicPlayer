package tech.soit.quiet.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main_music.*
import kotlinx.android.synthetic.main.item_main_navigation.view.*
import tech.soit.quiet.R
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.submitEmpty
import tech.soit.quiet.ui.item.withEmptyBinder
import tech.soit.quiet.ui.item.withLoadingBinder
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.typed.adapter.TypedAdapter

/**
 * main Fragment
 */
@LayoutId(R.layout.fragment_main_music)
class MainMusicFragment : BaseFragment() {


    private lateinit var pagerAdapter: PagerPlaylistAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init navigation items
        initNavigation()

        //contract tab layout with view pager
        pagerAdapter = PagerPlaylistAdapter(view.context)
        pagerPlayLists.adapter = pagerAdapter
        pagerPlayLists.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayoutPlayLists))
        tabLayoutPlayLists.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pagerPlayLists))

        //init data
        pagerAdapter.adapters[0].submitEmpty()
        pagerAdapter.adapters[1].submitEmpty()
    }

    private fun initNavigation() {
        with(navLayoutLocal) {
            imageIcon.setImageResource(R.drawable.ic_music_note_black_24dp)
            textTitle.setText(R.string.nav_local_music)
        }

        with(navLayoutHistory) {
            imageIcon.setImageResource(R.drawable.ic_history_black_24dp)
            textTitle.setText(R.string.nav_history)
        }

        with(navLayoutDownload) {
            imageIcon.setImageResource(R.drawable.ic_file_download_black_24dp)
            textTitle.setText(R.string.nav_download)
        }

        with(navLayoutCollection) {
            imageIcon.setImageResource(R.drawable.ic_collections_black_24dp)
            textTitle.setText(R.string.nav_collection)
        }

    }


    inner class PagerPlaylistAdapter(context: Context) : PagerAdapter() {

        val adapters = Array(2) {
            TypedAdapter()
                    .withLoadingBinder()
                    .withEmptyBinder()
        }

        private val recyclers = Array(2) {
            val recyclerView = RecyclerView(context)
            recyclerView.adapter = adapters[it]
            recyclerView.layoutManager = LinearLayoutManager(context)
            return@Array recyclerView
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = recyclers[position]
            container.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(recyclers[position])
        }

        override fun getCount(): Int {
            return 2
        }
    }

}