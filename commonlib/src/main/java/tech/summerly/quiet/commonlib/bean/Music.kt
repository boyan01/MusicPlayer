package tech.summerly.quiet.commonlib.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/26
 * desc   : 被播放的基本单位. 所有的不管是来自本地,还是来自网络的音乐,都得转换为此对象,
 *          才能被 MusicPlayerService 播放
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Music(
        val id: Long,
        val title: String,
        val artist: List<Artist>,
        val album: Album,
        val picUri: String?,
        val type: MusicType,
        val mvId: Long,
        val duration: Long,
        val playUri: List<MusicUri>
        ) : Parcelable {

    @Transient
    var isFavorite: Boolean = false

    fun toShortString(): String = "$id : $title"
    fun artistAlbumString(): String = "${album.name} - ${artist.joinToString("/")}"
}