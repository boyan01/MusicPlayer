package tech.soit.quiet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.repository.db.entity.LocalMusic
import tech.soit.quiet.utils.component.support.switchMap
import tech.soit.quiet.utils.testing.OpenForTesting

@OpenForTesting
class LocalAlbumDetailViewModel(localMusicDao: LocalMusicDao) : ViewModel() {

    protected val _album = MutableLiveData<Album>()

    final val album get() = _album

    /**
     * the musics associated with [album]
     */
    val musics: LiveData<List<LocalMusic>> = album.switchMap { album ->
        album ?: return@switchMap null
        return@switchMap localMusicDao.getMusicsByAlbum(album.getName())
    }

}