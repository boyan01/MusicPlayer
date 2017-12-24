package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.local.LocalMusicApi

/**
 * Created by summer on 17-12-24
 */
class LocalArtistFragment : SimpleLocalFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            val artist = LocalMusicApi.getLocalMusicApi(getContext()!!).getArtists()
            showItems(artist)
        }
    }

    override fun getSpanCount() = 2


}