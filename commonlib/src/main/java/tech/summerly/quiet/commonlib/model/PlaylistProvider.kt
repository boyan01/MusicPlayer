package tech.summerly.quiet.commonlib.model

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.utils.string

/**
 * 提供歌单信息
 *
 * 包括 ： 音乐列表、创建者....
 */
interface PlaylistProvider : Parcelable {

    suspend fun getMusicList(): List<Music>

    /**
     * 播放列表说明
     *
     * 如果不想显示歌单的其他信息,直接返回 null 即可
     */
    suspend fun getDescription(): Description?

    /**
     * 歌曲列表详情 Activity 的 title ,显示在 Toolbar 中.
     */
    val title get() = string(R.string.default_playlist_title)

    interface Description {
        //id
        val id: Long
        //歌单名
        val name: String
        //歌单封面图片
        val coverImgUrl: String
        //是否收藏
        val subscribed: Boolean
        //歌曲数目
        val trackCount: Int
        //创建时间
        val createTime: Long
        //播放次数
        val playCount: Long
        //type
        val type: MusicType
    }


    class SimpleDescription(
            override val id: Long,
            override val name: String,
            override val coverImgUrl: String,
            override val subscribed: Boolean,
            override val trackCount: Int,
            override val createTime: Long,
            override val playCount: Long,
            override val type: MusicType
    ) : PlaylistProvider.Description, Parcelable {

        constructor(source: Parcel) : this(
                source.readLong(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                source.readInt(),
                source.readLong(),
                source.readLong(),
                MusicType.values()[source.readInt()]
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeLong(id)
            writeString(name)
            writeString(coverImgUrl)
            writeInt((if (subscribed) 1 else 0))
            writeInt(trackCount)
            writeLong(createTime)
            writeLong(playCount)
            writeInt(type.ordinal)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SimpleDescription> = object : Parcelable.Creator<SimpleDescription> {
                override fun createFromParcel(source: Parcel): SimpleDescription = SimpleDescription(source)
                override fun newArray(size: Int): Array<SimpleDescription?> = arrayOfNulls(size)
            }
        }
    }

}