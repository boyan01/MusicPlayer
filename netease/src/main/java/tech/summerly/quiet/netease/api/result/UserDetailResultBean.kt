package tech.summerly.quiet.netease.api.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/25
 * desc   :
 */

data class UserDetailResultBean(

        @SerializedName("profile")
        @Expose
        val profile: Profile? = null,

        @SerializedName("level")
        @Expose
        val level: Int? = null,

        @SerializedName("listenSongs")
        @Expose
        val listenSongs: Long? = null,

        @SerializedName("userPoint")
        @Expose
        val userPoint: UserPoint? = null,

        //        @SerializedName("mobileSign")
//        @Expose
//        val mobileSign: Boolean? = null,
//        @SerializedName("pcSign")
//        @Expose
//        val pcSign: Boolean? = null,

//        @SerializedName("peopleCanSeeMyPlayRecord")
//        @Expose
//        val peopleCanSeeMyPlayRecord: Boolean? = null,

//        @SerializedName("bindings")
//        @Expose
//        val bindings: List<Binding>? = null,

        @SerializedName("adValid")
        @Expose
        val adValid: Boolean? = null,

        @SerializedName("code")
        @Expose
        val code: Long,

        @SerializedName("createDays")
        @Expose
        val createDays: Long? = null
) {

    data class Profile(


            @SerializedName("userId")
            @Expose
            val userId: Long? = null,
            @SerializedName("nickname")
            @Expose
            val nickname: String? = null,
            @SerializedName("avatarUrl")
            @Expose
            val avatarUrl: String? = null,
            @SerializedName("backgroundUrl")
            @Expose
            val backgroundUrl: String? = null,
            @SerializedName("playlistCount")
            @Expose
            val playlistCount: Long? = null


//            @SerializedName("followed")
//            @Expose
//            val followed: Boolean? = null,
//            @SerializedName("djStatus")
//            @Expose
//            val djStatus: Long? = null,
//            @SerializedName("detailDescription")
//            @Expose
//            val detailDescription: String? = null,
//            @SerializedName("avatarImgIdStr")
//            @Expose
//            val avatarImgIdStr: String? = null,
//            @SerializedName("backgroundImgIdStr")
//            @Expose
//            val backgroundImgIdStr: String? = null,
//            @SerializedName("description")
//            @Expose
//            val description: String? = null,
//            @SerializedName("accountStatus")
//            @Expose
//            val accountStatus: Long? = null,
//            @SerializedName("province")
//            @Expose
//            val province: Long? = null,
//            @SerializedName("defaultAvatar")
//            @Expose
//            val defaultAvatar: Boolean? = null,
//            @SerializedName("gender")
//            @Expose
//            val gender: Long? = null,
//            @SerializedName("birthday")
//            @Expose
//            val birthday: Long? = null,
//            @SerializedName("city")
//            @Expose
//            val city: Long? = null,
//            @SerializedName("mutual")
//            @Expose
//            val mutual: Boolean? = null,
//            @SerializedName("remarkName")
//            @Expose
//            val remarkName: Any? = null,
//            @SerializedName("experts")
//            @Expose
//            val experts: Experts? = null,
//            @SerializedName("avatarImgId")
//            @Expose
//            val avatarImgId: Long? = null,
//            @SerializedName("backgroundImgId")
//            @Expose
//            val backgroundImgId: Long? = null,
//            @SerializedName("expertTags")
//            @Expose
//            val expertTags: Any? = null,
//            @SerializedName("vipType")
//            @Expose
//            val vipType: Long? = null,
//            @SerializedName("userType")
//            @Expose
//            val userType: Long? = null,
//            @SerializedName("authStatus")
//            @Expose
//            val authStatus: Long? = null,
//            @SerializedName("signature")
//            @Expose
//            val signature: String? = null,
//            @SerializedName("authority")
//            @Expose
//            val authority: Long? = null,
//            @SerializedName("followeds")
//            @Expose
//            val followeds: Long? = null,
//            @SerializedName("follows")
//            @Expose
//            val follows: Long? = null,
//            @SerializedName("blacklist")
//            @Expose
//            val blacklist: Boolean? = null,
//            @SerializedName("eventCount")
//            @Expose
//            val eventCount: Long? = null,
//            @SerializedName("playlistBeSubscribedCount")
//            @Expose
//            val playlistBeSubscribedCount: Long? = null

    )

//    data class Binding(
//
//            @SerializedName("expiresIn")
//            @Expose
//            val expiresIn: Long? = null,
//            @SerializedName("refreshTime")
//            @Expose
//            val refreshTime: Long? = null,
//            @SerializedName("userId")
//            @Expose
//            val userId: Long? = null,
//            @SerializedName("tokenJsonStr")
//            @Expose
//            val tokenJsonStr: String? = null,
//            @SerializedName("url")
//            @Expose
//            val url: String? = null,
//            @SerializedName("expired")
//            @Expose
//            val expired: Boolean? = null,
//            @SerializedName("id")
//            @Expose
//            val id: Long? = null,
//            @SerializedName("type")
//            @Expose
//            val type: Long? = null
//    )

    data class UserPoint(

            @SerializedName("userId")
            @Expose
            val userId: Long? = null,
            @SerializedName("balance")
            @Expose
            val balance: Long? = null,
            @SerializedName("updateTime")
            @Expose
            val updateTime: Long? = null,
            @SerializedName("version")
            @Expose
            val version: Long? = null,
            @SerializedName("status")
            @Expose
            val status: Long? = null,
            @SerializedName("blockBalance")
            @Expose
            val blockBalance: Long? = null

    )
}

