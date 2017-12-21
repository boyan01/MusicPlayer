package tech.summerly.quiet.netease.api.result

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/24
 * desc   :
 */
data class PlaylistResultBean(val more: Boolean?,
                              val playlist: List<PlaylistBean>?,
                              val code: Int) {

    data class PlaylistBean(
            //            val subscribers: List<Any?>,
//                            val subscribed: Boolean,
//                            val creator: CreatorBean,
//                            var artists: Any?,
//                            var tracks: Any?,
//                            val status: Long,
//                            val subscribedCount: Long,
//                            val cloudTrackCount: Long,
//                            val ordered: Boolean,
//                            val tags: List<Any?>,
            var description: Any?,
            //                            val highQuality: Boolean,
            val updateTime: Long,
            val commentThreadId: String,
            //                            val privacy: Long,
            val newImported: Boolean,
            val coverImgId: Long,
            val createTime: Long,
            val trackUpdateTime: Long,
            val trackCount: Long,
            //                            val specialType: Long,
//                            val anonimous: Boolean,
//                            val adType: Long,
//                            val trackNumberUpdateTime: Long,
            val coverImgUrl: String,
            val playCount: Long,
            //                            val totalDuration: Long,
            val userId: Long,
            val name: String,
            val id: Long)



//        data class CreatorBean(val defaultAvatar: Boolean,
//                               val province: Long,
//                               val authStatus: Long,
//                               val followed: Boolean,
//                               val avatarUrl: String,
//                               val accountStatus: Long,
//                               val gender: Long,
//                               val city: Long,
//                               val birthday: Long,
//                               val userId: Long,
//                               val userType: Long,
//                               val nickname: String,
//                               val signature: String,
//                               val description: String,
//                               val detailDescription: String,
//                               val avatarImgId: Long,
//                               val backgroundImgId: Long,
//                               val backgroundUrl: String,
//                               val authority: Long,
//                               val mutual: Boolean,
//                               var expertTags: Any?,
//                               val djStatus: Long,
//                               val vipType: Long,
//                               var remarkName: Any?,
//                               val backgroundImgIdStr: String,
//                               val avatarImgIdStr: String)


}