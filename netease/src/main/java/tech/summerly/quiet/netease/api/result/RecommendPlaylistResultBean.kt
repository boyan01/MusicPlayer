package tech.summerly.quiet.netease.api.result

import kotlinx.serialization.SerialName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/25
 * desc   :
 */
data class RecommendPlaylistResultBean(
        @SerialName("code")
        val code: Int,
        @SerialName("featureFirst")
        val featureFirst: Boolean? = null,
        @SerialName("haveRcmdSongs")
        val haveRcmdSongs: Boolean? = null,
        @SerialName("recommend")
        val recommend: List<Recommend>? = null
) {
    data class Recommend(

            @SerialName("id")
            val id: Long,
            @SerialName("type")
            val type: Long? = null,
            @SerialName("title")
            val name: String,
            @SerialName("copywriter")
            val copywriter: String? = null,
            @SerialName("picUrl")
            val picUrl: String? = null,
            @SerialName("playcount")
            val playcount: Long? = null,
            @SerialName("createTime")
            val createTime: Long? = null,
            //            @SerialName("creator")
//
//            val creator: Creator? = null,
            @SerialName("trackCount")
            val trackCount: Long? = null
//            @SerialName("userId")
//
//            val userId: Long? = null,
//            @SerialName("alg")
//
//            val alg: String? = null

    )

//    data class Creator(
//
//            @SerialName("vipType")
//
//            val vipType: Long? = null,
//            @SerialName("province")
//
//            val province: Long? = null,
//            @SerialName("avatarImgId")
//
//            val avatarImgId: Long? = null,
//            @SerialName("backgroundImgId")
//
//            val backgroundImgId: Long? = null,
//            @SerialName("birthday")
//
//            val birthday: Long? = null,
//            @SerialName("city")
//
//            val city: Long? = null,
//            @SerialName("authStatus")
//
//            val authStatus: Long? = null,
//            @SerialName("detailDescription")
//
//            val detailDescription: String? = null,
//            @SerialName("defaultAvatar")
//
//            val defaultAvatar: Boolean? = null,
//            @SerialName("expertTags")
//
//            val expertTags: Any? = null,
//            @SerialName("djStatus")
//
//            val djStatus: Long? = null,
//            @SerialName("followed")
//
//            val followed: Boolean? = null,
//            @SerialName("mutual")
//
//            val mutual: Boolean? = null,
//            @SerialName("remarkName")
//
//            val remarkName: Any? = null,
//            @SerialName("avatarUrl")
//
//            val avatarUrl: String? = null,
//            @SerialName("backgroundUrl")
//
//            val backgroundUrl: String? = null,
//            @SerialName("nickname")
//
//            val nickname: String? = null,
//            @SerialName("avatarImgIdStr")
//
//            val avatarImgIdStr: String? = null,
//            @SerialName("backgroundImgIdStr")
//
//            val backgroundImgIdStr: String? = null,
//            @SerialName("description")
//
//            val description: String? = null,
//            @SerialName("userType")
//
//            val userType: Long? = null,
//            @SerialName("userId")
//
//            val userId: Long? = null,
//            @SerialName("accountStatus")
//
//            val accountStatus: Long? = null,
//            @SerialName("gender")
//
//            val gender: Long? = null,
//            @SerialName("signature")
//
//            val signature: String? = null,
//            @SerialName("authority")
//
//            val authority: Long? = null
//
//    )
}