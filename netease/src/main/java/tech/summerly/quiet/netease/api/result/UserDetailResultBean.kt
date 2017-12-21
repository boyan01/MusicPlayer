package tech.summerly.quiet.netease.api.result

import kotlinx.serialization.SerialName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/25
 * desc   :
 */

data class UserDetailResultBean(

        @SerialName("profile")
        val profile: Profile? = null,

        @SerialName("level")
        val level: Int? = null,

        @SerialName("listenSongs")
        val listenSongs: Long? = null,

        @SerialName("userPoint")
        val userPoint: UserPoint? = null,

        //        @SerialName("mobileSign")
//        val mobileSign: Boolean? = null,
//        @SerialName("pcSign")
//        val pcSign: Boolean? = null,

//        @SerialName("peopleCanSeeMyPlayRecord")
//        val peopleCanSeeMyPlayRecord: Boolean? = null,

//        @SerialName("bindings")
//        val bindings: List<Binding>? = null,

        @SerialName("adValid")
        val adValid: Boolean? = null,

        @SerialName("code")
        val code: Long,

        @SerialName("createDays")
        val createDays: Long? = null
) {

    data class Profile(


            @SerialName("userId")
            val userId: Long? = null,
            @SerialName("nickname")
            val nickname: String? = null,
            @SerialName("avatarUrl")
            val avatarUrl: String? = null,
            @SerialName("backgroundUrl")
            val backgroundUrl: String? = null,
            @SerialName("playlistCount")
            val playlistCount: Long? = null


//            @SerialName("followed")
//            val followed: Boolean? = null,
//            @SerialName("djStatus")
//            val djStatus: Long? = null,
//            @SerialName("detailDescription")
//            val detailDescription: String? = null,
//            @SerialName("avatarImgIdStr")
//            val avatarImgIdStr: String? = null,
//            @SerialName("backgroundImgIdStr")
//            val backgroundImgIdStr: String? = null,
//            @SerialName("description")
//            val description: String? = null,
//            @SerialName("accountStatus")
//            val accountStatus: Long? = null,
//            @SerialName("province")
//            val province: Long? = null,
//            @SerialName("defaultAvatar")
//            val defaultAvatar: Boolean? = null,
//            @SerialName("gender")
//            val gender: Long? = null,
//            @SerialName("birthday")
//            val birthday: Long? = null,
//            @SerialName("city")
//            val city: Long? = null,
//            @SerialName("mutual")
//            val mutual: Boolean? = null,
//            @SerialName("remarkName")
//            val remarkName: Any? = null,
//            @SerialName("experts")
//            val experts: Experts? = null,
//            @SerialName("avatarImgId")
//            val avatarImgId: Long? = null,
//            @SerialName("backgroundImgId")
//            val backgroundImgId: Long? = null,
//            @SerialName("expertTags")
//            val expertTags: Any? = null,
//            @SerialName("vipType")
//            val vipType: Long? = null,
//            @SerialName("userType")
//            val userType: Long? = null,
//            @SerialName("authStatus")
//            val authStatus: Long? = null,
//            @SerialName("signature")
//            val signature: String? = null,
//            @SerialName("authority")
//            val authority: Long? = null,
//            @SerialName("followeds")
//            val followeds: Long? = null,
//            @SerialName("follows")
//            val follows: Long? = null,
//            @SerialName("blacklist")
//            val blacklist: Boolean? = null,
//            @SerialName("eventCount")
//            val eventCount: Long? = null,
//            @SerialName("playlistBeSubscribedCount")
//            val playlistBeSubscribedCount: Long? = null

    )

//    data class Binding(
//
//            @SerialName("expiresIn")
//            val expiresIn: Long? = null,
//            @SerialName("refreshTime")
//            val refreshTime: Long? = null,
//            @SerialName("userId")
//            val userId: Long? = null,
//            @SerialName("tokenJsonStr")
//            val tokenJsonStr: String? = null,
//            @SerialName("url")
//            val url: String? = null,
//            @SerialName("expired")
//            val expired: Boolean? = null,
//            @SerialName("id")
//            val id: Long? = null,
//            @SerialName("type")
//            val type: Long? = null
//    )

    data class UserPoint(

            @SerialName("userId")
            val userId: Long? = null,
            @SerialName("balance")
            val balance: Long? = null,
            @SerialName("updateTime")
            val updateTime: Long? = null,
            @SerialName("version")
            val version: Long? = null,
            @SerialName("status")
            val status: Long? = null,
            @SerialName("blockBalance")
            val blockBalance: Long? = null

    )
}

