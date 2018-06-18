package tech.summerly.quiet.commonlib.bean

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.model.IMusic


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/26
 * desc   : 被播放的基本单位. 所有的不管是来自本地,还是来自网络的音乐,都得转换为此对象,
 *          才能被 MusicPlayerService 播放
 */
open class Music(
        override val id: Long,
        override val title: String,
        override val artist: List<Artist>,
        override val album: Album,
        val picUri: String?,
        val type: MusicType,
        val mvId: Long,
        override val duration: Long,
        val playUri: MutableList<MusicUri>
) : Parcelable, IMusic {


    override val artwork: String?
        get() = picUri

    override val isFavorite: Boolean
        get() = false

    override fun getUrl(bitrate: Int): String {
        return ""
    }

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

        const val ID_NONE = 0L
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Music

        if (id != other.id) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }


    override suspend fun delete() {

    }

    override suspend fun like() {

    }
}