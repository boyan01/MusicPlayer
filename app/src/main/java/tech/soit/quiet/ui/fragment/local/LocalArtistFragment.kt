package tech.soit.quiet.ui.fragment.local

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity.Companion.ARG_OBJ
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity.Companion.ARG_TYPE
import tech.soit.quiet.ui.activity.local.LocalMusicListActivity.Companion.TYPE_ARTIST
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.Empty
import tech.soit.quiet.ui.item.Loading
import tech.soit.quiet.utils.submit
import tech.soit.quiet.utils.withBinder
import tech.soit.quiet.utils.withEmptyBinder
import tech.soit.quiet.utils.withLoadingBinder
import tech.soit.quiet.viewmodel.LocalMusicViewModel

/**
 *
 * display local artist list
 *
 * @author : summer
 * @date : 18-9-1
 */
class LocalArtistFragment : BaseFragment() {


    private val viewModel by lazyViewModel<LocalMusicViewModel>()

    private val adapter = MultiTypeAdapter()
            .withBinder(AItemViewBinder(onClick = this::onArtistItemClicked))
            .withEmptyBinder()
            .withLoadingBinder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.allArtists.observe(this, Observer { artists ->
            when {
                artists == null -> adapter.submit(listOf(Loading))
                artists.isEmpty() -> adapter.submit(listOf(Empty))
                else -> adapter.submit(artists.map { AItem(it.name, "") })
            }
        })
    }

    override fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = RecyclerView(inflater.context)
        recyclerView.id = R.id.recyclerView
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = (view as ViewGroup)[0] as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
    }

    /**
     * callback when item clicked
     */
    private fun onArtistItemClicked(position: Int) {
        val artist = adapter.items[position] as Artist
        val intent = Intent(context, LocalMusicListActivity::class.java)
        intent.putExtra(ARG_TYPE, TYPE_ARTIST)
        intent.putExtra(ARG_OBJ, artist)
        startActivity(intent)
    }

}