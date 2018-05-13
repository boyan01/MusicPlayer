package tech.summerly.quiet.playlistdetail

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pd_fragment_internal_search.view.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.color
import tech.summerly.quiet.commonlib.utils.setItems2
import tech.summerly.quiet.playlistdetail.items.MusicViewBinder

/**
 * Created by summer on 18-3-18
 */
class PlaylistInternalSearchFragment : BaseFragment() {

    companion object {

        private const val KEY_MUSIC_LIST = "musics"

        fun getInstance(musics: ArrayList<Music>) = PlaylistInternalSearchFragment().also {
            it.arguments = Bundle().apply {
                putParcelableArrayList(KEY_MUSIC_LIST, musics)
            }
        }
    }

    private val adapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(Music::class.java, MusicViewBinder().withOnItemClickListener(onMusicClick))
        adapter
    }

    private val musicList = ArrayList<Music>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicList.addAll(getArgument(KEY_MUSIC_LIST))
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
        view?.searchView?.clearFocus()
        super.onStop()
    }

    private val onMusicClick = fun(music: Music) {
        MusicPlayerManager.play(musicList, music)
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
        val data = musicList
                .filter {
                    it.title.contains(text, true)
                            || it.artistAlbumString().contains(text, true)
                }
        adapter.setItems2(data, detectDiff = false)
    }

}