package tech.soit.quiet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.repository.db.entity.LocalMusic
import tech.soit.quiet.utils.component.support.map
import tech.soit.quiet.utils.component.support.mapNonNull


/**
 * view model tracks LocalMusicRepository
 */
class LocalMusicViewModel constructor(
        private val localMusicDao: LocalMusicDao
) : ViewModel() {

    /**
     * local total musics
     */
    val allMusics: LiveData<List<Music>>
        get() = localMusicDao.getAllMusics()
                .map { localMusics ->
                    if (localMusics == null) {
                        return@map null
                    }
                    localMusics.map(LocalMusic::toMusic)
                }


    /**
     * local total albums
     */
    val allAlbums: LiveData<List<Album>>
        get() = allMusics.mapNonNull { musics ->
            musics.map { it.album }.distinctBy { it.title }
        }

    /**
     * local total artist
     */
    val allArtists: LiveData<List<Artist>>
        get() = allMusics.mapNonNull { musics ->
            musics.flatMap { it.artists }.distinctBy { it.name }
        }

}