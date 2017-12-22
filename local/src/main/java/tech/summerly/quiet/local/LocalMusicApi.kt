package tech.summerly.quiet.local

import android.content.Context
import tech.summerly.quiet.local.database.database.LocalMusicDatabase

/**
 * Created by summer on 17-12-21
 */
class LocalMusicApi private constructor(context: Context) {
    companion object {
        fun getLocalMusicApi(context: Context) = LocalMusicApi(context.applicationContext)
    }

    private val musicDao = LocalMusicDatabase.getInstance(context).musicDao()

}