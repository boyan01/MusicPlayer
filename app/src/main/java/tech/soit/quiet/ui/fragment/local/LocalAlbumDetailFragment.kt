package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_local_album_detail.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.ui.drawable.ArcColorDrawable
import tech.soit.quiet.ui.fragment.base.BottomControllerFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.viewmodel.LocalAlbumDetailViewModel
import tech.soit.typed.adapter.TypedAdapter

@LayoutId(R.layout.fragment_local_album_detail, translucent = false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        appBarLayout.background = ArcColorDrawable(view.context.attrValue(R.attr.colorPrimary))
        (appBarLayout.layoutParams as CoordinatorLayout.LayoutParams).apply {
            behavior = object : AppBarLayout.Behavior() {

                override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
                    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
                    val drawable = appBarLayout.background as ArcColorDrawable
                    if (topAndBottomOffset == 0) {
                        drawable.pull(-dyUnconsumed.toFloat() / 2)
                    }
                }

                override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {
                    super.onStopNestedScroll(coordinatorLayout, abl, target, type)
                    (appBarLayout.background as ArcColorDrawable).release()
                }

            }
        }
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            (appBarLayout.background as ArcColorDrawable).absorb(-offset.toFloat())
        })
    }


}