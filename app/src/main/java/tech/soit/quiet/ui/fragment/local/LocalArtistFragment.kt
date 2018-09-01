package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.viewmodel.LocalMusicViewModel
import tech.soit.typed.adapter.TypedAdapter

/**
 * @author : summer
 * @date : 18-9-1
 */
class LocalArtistFragment : BaseFragment() {


    private val viewModel by lazyViewModel<LocalMusicViewModel>()

    private val adapter = TypedAdapter()
            .withBinder(Artist::class, CommonAItemBinder()) { CommonAItem(it.name, "") }
            .withEmptyBinder()
            .withLoadingBinder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.allArtists.observe(this, Observer { artists ->
            when {
                artists == null -> adapter.submit(listOf(Empty))
                artists.isEmpty() -> adapter.submit(listOf(Empty))
                else -> adapter.submit(artists)
            }
        })
    }

    override fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return RecyclerView(inflater.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = (view as ViewGroup)[0] as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
    }

}