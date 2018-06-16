package tech.summerly.quiet.commonlib.model

import java.io.Serializable

interface IMusic : Serializable {

    val id: Long

    val title: String

    val artist: List<IArtist>

    val album: IAlbum

    val duration: Long

    fun getUrl(bitrate: Int = 320_000): String

    val isFavorite: Boolean

    val artwork: String

    suspend fun delete()

    suspend fun like()
}