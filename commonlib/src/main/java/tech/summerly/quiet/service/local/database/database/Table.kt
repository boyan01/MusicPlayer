package tech.summerly.quiet.service.local.database.database

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import tech.summerly.quiet.commonlib.utils.observeFilterNull

/**
 * Created by summer on 18-1-9
 */
enum class Table {
    Music,
    Artist,
    Album,
    Playlist,
    PlaylistMusic;

    private val observer = MutableLiveData<Long>()

    fun listenChange(owner: LifecycleOwner, observer: (Long) -> Unit) {
        this.observer.observeFilterNull(owner, observer)
    }

    fun postChange() = observer.postValue(System.currentTimeMillis())

}