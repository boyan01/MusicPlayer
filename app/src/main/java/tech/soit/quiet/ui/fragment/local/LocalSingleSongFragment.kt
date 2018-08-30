package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_local_single_song.*
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.viewmodel.LocalMusicViewModel
import tech.soit.typed.adapter.TypedAdapter

@LayoutId(R.layout.fragment_local_single_song)
class LocalSingleSongFragment : BaseFragment() {

    companion object {

        private const val TOKEN_PLAYLIST = "local_single_song"

    }

    private val viewModel by lazyViewModel<LocalMusicViewModel>()

    private val adapter = TypedAdapter()
            .withBinder(Loading::class, LoadingViewBinder())
            .withBinder(Empty::class, EmptyViewBinder())
            .withBinder(Music::class, MusicItemBinder(TOKEN_PLAYLIST, this::onMusicItemClick))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
    }

    init {
        viewModelFactory = object : ViewModelProvider.AndroidViewModelFactory(AppContext) {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LocalMusicViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LocalMusicViewModel(QuietDatabase.instance.localMusicDao()) as T
                }
                return super.create(modelClass)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.allMusics.observe(this, Observer { musics: List<Music>? ->
            if (musics == null) {
                adapter.submit(listOf(Loading))
            } else if (musics.isEmpty()) {
                adapter.submit(listOf(Empty))
            } else {
                adapter.submit(musics)
            }
        })
    }


    /**
     * call back when MusicItem been clicked
     *
     * @param view the item view
     * @param music the item
     */
    private fun onMusicItemClick(view: View, music: Music) {
        log { "on click : $music" }
    }

}