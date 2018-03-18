package tech.summerly.quiet.playlistdetail

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.content.systemService
import kotlinx.android.synthetic.main.pd_fragment_internal_search.view.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.items.MusicItemViewBinder
import tech.summerly.quiet.commonlib.utils.color
import tech.summerly.quiet.commonlib.utils.setItems2

/**
 * Created by summer on 18-3-18
 */
class PlaylistInternalSearchFragment : BaseFragment() {

    companion object {

        private const val KEY_MUSIC_LIST = "musics"

        operator fun invoke(musics: ArrayList<Music>) = PlaylistInternalSearchFragment().also {
            it.arguments = Bundle().apply {
                putParcelableArrayList(KEY_MUSIC_LIST, musics)
            }
        }
    }


    private val adapter = MultiTypeAdapter()

    private val items = ArrayList<Music>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelableArrayList<Music>(KEY_MUSIC_LIST)?.let {
            items.addAll(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pd_fragment_internal_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.navigationIcon?.setTint(color(R.color.color_text_primary_dark_background))
        toolbar.setNavigationOnClickListener {
            closeSelf()
        }
        list.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterText(newText)
                return true
            }

        })
    }

    override fun onResume() {
        super.onResume()
        val searchView = view?.searchView ?: return
        searchView.isIconified = false
    }

    override fun onStop() {
        activity?.systemService<InputMethodManager>()?.let {
            val searchView = view?.searchView ?: return@let
            it.hideSoftInputFromWindow(searchView.windowToken, 0)
        }
        super.onStop()
    }

    /**
     * show filtered data
     */
    private fun filterText(text: CharSequence?) {
        if (text == null || text.isEmpty()) {
            if (adapter.items.isNotEmpty()) {
                val size = adapter.items.size
                adapter.items.clear()
                adapter.notifyItemRangeRemoved(0, size)
            }
            return
        }
        val data = items
                .filter {
                    it.title.contains(text, true)
                            || it.artistAlbumString().contains(text, true)
                }
        adapter.setItems2(data, detectDiff = false)
    }


    private val onMusicClick = fun(music: Music) {
        (activity as? PlaylistDetailActivity)?.onMusicClick?.invoke(music)
    }

    init {
        adapter.register(Music::class.java, MusicItemViewBinder(onMusicClick))
    }


}