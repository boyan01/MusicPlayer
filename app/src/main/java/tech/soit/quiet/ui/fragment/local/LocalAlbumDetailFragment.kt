package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_local_album_detail.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.ui.fragment.base.BottomControllerFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.viewmodel.LocalAlbumDetailViewModel
import tech.soit.typed.adapter.TypedAdapter

class LocalAlbumDetailFragment : BottomControllerFragment() {

    companion object {

        private const val ARG_ALBUM = "album"

        /**
         * create new instance of [LocalAlbumDetailFragment]
         */
        fun newInstance(album: Album): LocalAlbumDetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_ALBUM, album)
            return LocalAlbumDetailFragment().apply {
                arguments = bundle
            }
        }

    }

    private val viewModel by lazyViewModelInternal<LocalAlbumDetailViewModel>()

    private val album: Album
        get() = arguments?.getParcelable(ARG_ALBUM) ?: error("album is null")

    private val adapter by lazy {
        TypedAdapter()
                .withEmptyBinder()
                .withLoadingBinder()
                .withBinder(Music::class, MusicItemBinder(
                        token = "local_album_" + album.title,
                        onClick = { view, music ->

                        },
                        onPlayingItemShowHide = {

                        }
                ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.album.postValue(album)
        viewModel.musics.observe(this, Observer { musics ->
            when {
                musics == null -> adapter.submit(listOf(Loading))
                musics.isEmpty() -> adapter.submit(listOf(Empty))
                else -> adapter.submit(musics)
            }
        })
    }

    override fun onCreateView3(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_local_album_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        (toolbar.layoutParams as CoordinatorLayout.LayoutParams).apply {
            behavior = object : CoordinatorLayout.Behavior<View>() {

                override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
                    log { "depend : $child $dependency" }
                    return dependency.id == R.id.recyclerView
                }

                override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
                    return true
                }

                override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
                    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
                    log { "on nested scroll($type) : $dyConsumed ,  $dyUnconsumed " }
                }

            }
        }
    }


}