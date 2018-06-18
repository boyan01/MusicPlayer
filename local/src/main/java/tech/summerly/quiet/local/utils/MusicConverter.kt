package tech.summerly.quiet.local.utils

import com.mpatric.mp3agic.Mp3File
import org.jetbrains.anko.attempt
import tech.summerly.quiet.commonlib.bean.*
import tech.summerly.quiet.commonlib.utils.md5
import java.io.File

/**
 * Created by summer on 17-12-21
 */
object MusicConverter {

    private const val DEFAULT_ARTIST = "未知歌手"
    private const val DEFAULT_ALBUM = "未知专辑"
    private const val DEFAULT_DURATION = 0L
    private const val DEFAULT_BITRATE = 0

    fun scanFileToMusic(file: File): Music? {
        if (!file.exists()) {
            return null
        }
        val mp3File = attempt { Mp3File(file) }.value

        val title = mp3File.title(file)
        val artist = mp3File.artist()
        val picPath = mp3File.artWork()?.let {
            LocalMusicPictureUtils.saveEmbeddedPicture((title + artist).md5(), it, replaceIfExist = true)?.let { File(it) }
        }?.toURI()?.toString()
        val musicUri = MusicUri(bitrate = mp3File.bitrate(), uri = file.toURI().toString(), dateValid = Long.MAX_VALUE)
        return Music(
                id = 0,
                title = title,
                artist = Artist.fromString(artist, MusicType.LOCAL, picPath),
                album = Album(0, mp3File.album(), picPath, MusicType.LOCAL),
                picUri = picPath,
                type = MusicType.LOCAL,
                mvId = 0,
                duration = mp3File.duration(),
                playUri = mutableListOf(musicUri)
        ).also {
//            it.isFavorite = false
        }
    }

    private fun Mp3File?.duration(): Long = this?.lengthInMilliseconds ?: DEFAULT_DURATION

    private fun Mp3File?.bitrate(): Int = this?.bitrate ?: DEFAULT_BITRATE

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