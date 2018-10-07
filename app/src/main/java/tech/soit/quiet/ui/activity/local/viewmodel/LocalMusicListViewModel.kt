package tech.soit.quiet.ui.activity.local.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.repository.db.entity.LocalMusic
import tech.soit.quiet.utils.component.support.map
import tech.soit.quiet.utils.testing.OpenForTesting

@OpenForTesting
class LocalMusicListViewModel(
        private val dao: LocalMusicDao
) : ViewModel() {


    /**
     * get local music by artist
     */
    fun getMusicListByArtist(artist: Artist): LiveData<List<Music>> {
        return dao.getMusicsByArtist(artist.name).map { it?.map(LocalMusic::toMusic) }
    }


    /**
     * get local music by album
     */
    fun getMusicListByAlbum(album: Album): LiveData<List<Music>> {
        return dao.getMusicsByAlbum(album.title)
                .map { it?.map(LocalMusic::toMusic) }
    }


}