package tech.summerly.quiet.local.viewmodel

import android.arch.lifecycle.ViewModel
import tech.summerly.quiet.local.repository.database.LocalMusicDatabase

internal class LocalMusicViewModel : ViewModel() {

    companion object {

        val instance by lazy { LocalMusicViewModel() }

    }

    private val database = LocalMusicDatabase.instance


    val allArtist get() = database.artistDao().allArtist()

    val allMusic get() = database.musicDao().allMusic()

    val allAlbum get() = database.albumDao().allAlbum()

}