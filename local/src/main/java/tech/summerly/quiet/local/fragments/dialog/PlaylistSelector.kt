package tech.summerly.quiet.local.fragments.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_fragment_playlist_selector.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.support.v4.toast
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.bean.Playlist
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.commonlib.utils.setItemsByDiff
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.LocalMusicApi
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.database.database.Table
import tech.summerly.quiet.local.fragments.items.LocalPlaylistItemViewBinder
import tech.summerly.quiet.local.utils.showPlaylistCreatorDialog

internal class LocalPlaylistSelectorFragment : BottomSheetDialogFragment() {

    companion object {

        private val KEY_MUSICS = "musics"

        operator fun invoke(musics: Array<Music>): LocalPlaylistSelectorFragment {
            return LocalPlaylistSelectorFragment().also {
                it.arguments = Bundle().also {
                    it.putParcelableArray(KEY_MUSICS, musics)
                }
            }
        }
    }

    private var version = System.currentTimeMillis()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.local_fragment_playlist_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        val changeObserver = { newVersion: Long ->
            if (newVersion > version) {
                refreshData()
                version = newVersion
            }
        }
        Table.Playlist.listenChange(this@LocalPlaylistSelectorFragment, changeObserver)
        Table.PlaylistMusic.listenChange(this@LocalPlaylistSelectorFragment, changeObserver)
        fabAdd.setOnClickListener {
            context.showPlaylistCreatorDialog()
        }
        listPlaylist.adapter = MultiTypeAdapter(mutableListOf<Any>()).also {
            it.register(Playlist::class.java, LocalPlaylistItemViewBinder(this@LocalPlaylistSelectorFragment::onPlaylistItemClicked))
        }
        refreshData()
    }

    private fun refreshData() {
        val view = this.view ?: return
        launch(UI) {
            val playlists = LocalMusicApi.getLocalMusicApi(LocalModule).getPlaylists().await()
            view.listPlaylist.multiTypeAdapter.setItemsByDiff(playlists)
        }
    }

    private fun onPlaylistItemClicked(playlist: Playlist) {
        val musics = arguments?.getParcelableArray(KEY_MUSICS) ?: return
        launch {
            @Suppress("UNCHECKED_CAST")//this cast is safely
            playlist.insertMusic(musics as Array<Music>)
            launch(UI) {
                toast(getString(R.string.local_message_insert_playlist_succeed))
                dismiss()
            }
        }
    }
}


private fun Playlist.insertMusic(musics: Array<Music>) {
    if (type != MusicType.LOCAL) {
        return
    }
    LocalMusicApi.getLocalMusicApi(LocalModule)
            .insertMusic(this, musics)
}