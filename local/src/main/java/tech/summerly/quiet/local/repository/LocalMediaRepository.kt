package tech.summerly.quiet.local.repository

import android.provider.MediaStore
import tech.summerly.quiet.commonlib.model.IAlbum
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.model.LocalMusic

internal class LocalMediaRepository {


    companion object {

        val MUSIC_PROJECTION = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.IS_MUSIC)

    }

    fun albums(): List<IAlbum> {
        TODO()
    }


    fun musics(): List<LocalMusic> {

        val cursor = LocalModule.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MUSIC_PROJECTION,
                null,
                null,
                null,
                null)
        cursor.close()
        TODO()
    }

}