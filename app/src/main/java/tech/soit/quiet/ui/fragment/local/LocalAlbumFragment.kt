package tech.soit.quiet.ui.fragment.local

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity.Companion.ARG_OBJ
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity.Companion.ARG_TYPE
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity.Companion.TYPE_ALBUM
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.*
import tech.soit.quiet.viewmodel.LocalMusicViewModel

/**
 * @author : summer
 * @date : 18-9-1
 */
class LocalAlbumFragment : BaseFragment() {


    private val viewModel by lazyViewModel<LocalMusicViewModel>()

    private val adapter = MultiTypeAdapter()
            .withBinder(AItemViewBinder(onClick = this::onAlbumClick))
            .withEmptyBinder()
            .withLoadingBinder()

    private var albums: List<Album> = emptyList()

    private fun onAlbumClick(position: Int) {
        val intent = Intent(context, LocalMusicListActivity::class.java)
        intent.putExtra(ARG_TYPE, TYPE_ALBUM)
        intent.putExtra(ARG_OBJ, albums[position] as Parcelable)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.allAlbums.observe(this, Observer { albums ->
            when {
                albums == null -> adapter.submit(listOf(Loading))
                albums.isEmpty() -> adapter.submit(listOf(Empty))
                else -> {
                    adapter.submit(albums.map { AItem(it.getName(), "") })
                    this.albums = albums
                }
            }
        })
    }

    override fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = RecyclerView(inflater.context)
        view.id = R.id.recyclerView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = (view as ViewGroup)[0] as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
    }

}