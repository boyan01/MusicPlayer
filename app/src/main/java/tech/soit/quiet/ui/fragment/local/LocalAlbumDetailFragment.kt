package tech.soit.quiet.ui.fragment.local

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
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
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.dimen
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

    private val headerHeight = dimen(R.dimen.playlist_detail_header_height)

    private lateinit var album: Album

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
        album = arguments?.getParcelable(ARG_ALBUM) ?: error("album is null")
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

        toolbar.title = album.title
        toolbar.background = ColorDrawable(toolbar.attrValue(R.attr.colorPrimary)).apply {
            alpha = 0
        }

        recyclerView.adapter = adapter
        appBarLayout.background = ArcColorDrawable(view.attrValue(R.attr.colorAccent))
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            log { "verticalOffset = $verticalOffset" }
            toolbar.translationY = -verticalOffset.toFloat()

            //the alpha of toolbar's background
            val alphaB = ((-verticalOffset.toFloat() / (appBarLayout.height))) * 255
            toolbar.background.alpha = alphaB.toInt()
        })
    }

}