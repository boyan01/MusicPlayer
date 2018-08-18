package tech.soit.quiet.player.core

import android.media.MediaPlayer
import tech.soit.quiet.model.vo.Music
import javax.sql.DataSource

suspend fun MediaPlayer.setDataSource(music: Music) {
    TODO()
}

class MediaDataSource(dataSource: DataSource) : DataSource by dataSource {

    companion object {

        fun with(url: String): MediaDataSource {
            TODO()
        }
    }

    private object DefaultNameGenerator : (String) -> String {
        override fun invoke(url: String): String {
            TODO()
        }
    }
}

