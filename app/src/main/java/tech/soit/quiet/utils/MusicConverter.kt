package tech.soit.quiet.utils

import androidx.core.net.toUri
import com.mpatric.mp3agic.Mp3File
import tech.soit.quiet.repository.db.entity.LocalMusic
import tech.summerly.quiet.commonlib.utils.log
import java.io.File

/**
 *
 * convert file to [LocalMusic]
 *
 * @author 伯言
 */
object MusicConverter {

    private const val DEFAULT_ARTIST = "未知歌手"
    private const val DEFAULT_ALBUM = "未知专辑"

    private const val DEFAULT_DURATION = 0L
    private const val DEFAULT_BITRATE = 0

    fun scanFileToMusic(file: File): LocalMusic? {
        if (!file.exists()) {
            return null
        }
        val mp3File: Mp3File?
        try {
            mp3File = Mp3File(file)
        } catch (e: Exception) {
            log { "can not convert file to Mp3File : ${file.path}" }
            return null
        }

        val title = mp3File.title(file)
        val artist = mp3File.artist()
        val album = mp3File.album()
        return LocalMusic(0, file.toUri().toString(), title, album, artist)
    }

    private fun Mp3File?.album(): String {
        this ?: return DEFAULT_ALBUM
        return id3v2Tag?.album ?: id3v1Tag?.album ?: DEFAULT_ALBUM
    }

    private fun Mp3File?.artist(): String {
        this ?: return DEFAULT_ARTIST
        return id3v2Tag?.artist ?: id3v1Tag?.artist ?: DEFAULT_ARTIST
    }

    private fun Mp3File?.title(file: File): String = this?.id3v2Tag?.title
            ?: this?.id3v1Tag?.title
            ?: file.nameWithoutExtension


    private fun Mp3File?.artWork(): ByteArray? = this?.id3v2Tag?.albumImage

}