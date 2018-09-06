package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.viewmodel.LocalMusicViewModel
import tech.soit.typed.adapter.TypedAdapter

/**
 * @author : summer
 * @date : 18-9-1
 */
class LocalAlbumFragment : BaseFragment() {


    private val viewModel by lazyViewModel<LocalMusicViewModel>()

    private val adapter = TypedAdapter()
            .withBinder(Album::class, CommonAItemBinder(
                    onClick = this::onAlbumClick
            )) { CommonAItem(it.title, "") }
            .withEmptyBinder()
            .withLoadingBinder()

    private fun onAlbumClick(position: Int) {
        //FIXME it's do not clever
        val album = adapter.list[position] as Album
        requireBaseActivity().open(LocalAlbumDetailFragment.newInstance(album))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.allAlbums.observe(this, Observer { albums ->
            when {
                albums == null -> adapter.submit(listOf(Loading))
                albums.isEmpty() -> adapter.submit(listOf(Empty))
                else -> adapter.submit(albums)
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