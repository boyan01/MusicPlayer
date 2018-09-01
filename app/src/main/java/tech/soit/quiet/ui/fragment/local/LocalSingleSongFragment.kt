package tech.soit.quiet.ui.fragment.local

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_local_single_song.*
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.ui.item.*
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.LoggerLevel
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
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

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
            when {
                musics == null -> adapter.submit(listOf(Loading))
                musics.isEmpty() -> adapter.submit(listOf(Empty))
                else -> adapter.submit(musics)
            }
        })
        //listen music change
        MusicPlayerManager.playingMusic.observe(this, Observer { playing: Music? ->
            if (MusicPlayerManager.musicPlayer.playlist.token != TOKEN_PLAYLIST) {
                return@Observer
            }
            if (playing == null) {
                adapter.notifyDataSetChanged()
            } else {
                val index = adapter.list.indexOf(playing)
                adapter.notifyItemChanged(index)
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
        val playlist = MusicPlayerManager.musicPlayer.playlist
        if (playlist.token == TOKEN_PLAYLIST && music == playlist.current) {
            //to open player fragment
            val player = MusicPlayerManager.musicPlayer.mediaPlayer
            if (!player.isPlayWhenReady) {
                player.isPlayWhenReady = true
            }
        } else {
            val musics = adapter.list.filterIsInstance(Music::class.java)
            if (musics.isEmpty()) {
                log(LoggerLevel.ERROR) { "item clicked, but list is still null! token : $TOKEN_PLAYLIST" }
            }
            MusicPlayerManager.play(TOKEN_PLAYLIST, music, musics)
        }
    }

}