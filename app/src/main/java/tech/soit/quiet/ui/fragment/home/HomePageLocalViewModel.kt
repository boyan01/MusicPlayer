package tech.soit.quiet.ui.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.repository.db.entity.LocalMusic
import tech.soit.quiet.utils.component.support.map

class HomePageLocalViewModel constructor(
        database: QuietDatabase = QuietDatabase.instance
) : ViewModel() {

    /**
     * local total musics
     */
    val allMusics: LiveData<List<Music>> =
            database.localMusicDao().getAllMusics()
                    .map { localMusics ->
                        localMusics.map(LocalMusic::toMusic)
                    }


    /**
     * local total albums
     */
    val allAlbums: LiveData<List<Album>> =
            allMusics.map { musics ->
                musics.map { it.album }.distinctBy { it.title }
            }

    /**
     * local total artist
     */
    val allArtists: LiveData<List<Artist>> =
            allMusics.map { musics ->
                musics.flatMap { it.artists }.distinctBy { it.name }
            }

}