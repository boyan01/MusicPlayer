package tech.summerly.quiet.commonlib.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.File
import java.net.URI


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/26
 * desc   : 被播放的基本单位. 所有的不管是来自本地,还是来自网络的音乐,都得转换为此对象,
 *          才能被 MusicPlayerService 播放
 */
open class Music(
        val id: Long,
        val title: String,
        val artist: List<Artist>,
        val album: Album,
        val picUri: String?,
        val type: MusicType,
        val mvId: Long,
        @Deprecated("不可信")
        val duration: Long,
        val playUri: MutableList<MusicUri>
) : Parcelable {

    @Transient
    var isFavorite: Boolean = false

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.createTypedArrayList(Artist.CREATOR),
            parcel.readParcelable(Album::class.java.classLoader),
            parcel.readString(),
            MusicType.valueOf(parcel.readString()),
            parcel.readLong(),
            parcel.readLong(),
            parcel.createTypedArrayList(MusicUri.CREATOR))

    fun toShortString(): String = "$id : $title - ${artist.joinToString("/") { it.name }}"
    fun artistAlbumString(): String = "${album.name} - ${artist.joinToString { it.name }}"
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeTypedList(artist)
        parcel.writeParcelable(album, flags)
        parcel.writeString(picUri)
        parcel.writeString(type.name)
        parcel.writeLong(mvId)
        parcel.writeLong(duration)
        parcel.writeTypedList(playUri)
    }

    /**
     * try to get an player url for music
     * this url could be file'path (local file) , http url (net resource)
     */
    open suspend fun getPlayableUrl(): String {
        if (playUri.isEmpty()) {
            error("no playable url!")
        }
        val uri = playUri[0].uri
        if (uri.startsWith("file:", true)) {
            val file = File(URI(uri))
            if (file.exists()) {
                return file.path
            } else {
                error("no playable url!")
            }
        }
        if (uri.startsWith("http:", true)) {
            return uri
        }
        error("no playable url!")
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Music(title='$title', mvId=$mvId, duration=$duration)"
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }

    fun getHighestQuality(): String? {
        playUri.sortByDescending { it.bitrate }
        if (playUri.isEmpty()) {
            return null
        }
        val bitrate = playUri[0].bitrate
        return when {
            bitrate in 224000..320000 -> {
                "HQ"
            }
            bitrate > 320000 -> {
                "SQ"
            }
            else -> null
        }
    }
}