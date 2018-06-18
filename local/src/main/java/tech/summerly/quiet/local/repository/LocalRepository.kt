package tech.summerly.quiet.local.repository

import android.content.Context
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.repository.database.LocalMusicDatabase
import tech.summerly.quiet.local.repository.entity.MusicEntity


internal object LocalRepository {



    private val context: Context get() = LocalModule


    private val musicDao = LocalMusicDatabase.instance.musicDao()


    fun insertMusic(music: MusicEntity) {
        musicDao.insertMusic(music)
    }

}