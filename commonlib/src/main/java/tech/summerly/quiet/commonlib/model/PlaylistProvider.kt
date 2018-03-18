package tech.summerly.quiet.commonlib.model

import android.os.Parcelable
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType

/**
 * 提供歌单信息
 *
 * 包括 ： 音乐列表、创建者....
 * TODO
 */
interface PlaylistProvider : Parcelable {

    suspend fun getMusicList(): List<Music>

    /**
     * 获取播放列表说明
     */
    suspend fun getDescription(): Description?

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
}