package tech.summerly.quiet.service.netease.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/25
 * desc   :
 */
data class RecommendPlaylistResultBean(

        @SerializedName("code")
        @Expose
        val code: Int,
        @SerializedName("featureFirst")
        @Expose
        val featureFirst: Boolean? = null,
        @SerializedName("haveRcmdSongs")
        @Expose
        val haveRcmdSongs: Boolean? = null,
        @SerializedName("recommend")
        @Expose
        val recommend: List<Recommend>? = null
) {
    data class Recommend(

            @SerializedName("id")
            @Expose
            val id: Long,
            @SerializedName("type")
            @Expose
            val type: Long? = null,
            @SerializedName("title")
            @Expose
            val name: String,
            @SerializedName("copywriter")
            @Expose
            val copywriter: String? = null,
            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,
            @SerializedName("playcount")
            @Expose
            val playcount: Long? = null,
            @SerializedName("createTime")
            @Expose
            val createTime: Long? = null,
            //            @SerializedName("creator")
//            @Expose
//            val creator: Creator? = null,
            @SerializedName("trackCount")
            @Expose
            val trackCount: Long? = null
//            @SerializedName("userId")
//            @Expose
//            val userId: Long? = null,
//            @SerializedName("alg")
//            @Expose
//            val alg: String? = null

    )

//    data class Creator(
//
//            @SerializedName("vipType")
//            @Expose
//            val vipType: Long? = null,
//            @SerializedName("province")
//            @Expose
//            val province: Long? = null,
//            @SerializedName("avatarImgId")
//            @Expose
//            val avatarImgId: Long? = null,
//            @SerializedName("backgroundImgId")
//            @Expose
//            val backgroundImgId: Long? = null,
//            @SerializedName("birthday")
//            @Expose
//            val birthday: Long? = null,
//            @SerializedName("city")
//            @Expose
//            val city: Long? = null,
//            @SerializedName("authStatus")
//            @Expose
//            val authStatus: Long? = null,
//            @SerializedName("detailDescription")
//            @Expose
//            val detailDescription: String? = null,
//            @SerializedName("defaultAvatar")
//            @Expose
//            val defaultAvatar: Boolean? = null,
//            @SerializedName("expertTags")
//            @Expose
//            val expertTags: Any? = null,
//            @SerializedName("djStatus")
//            @Expose
//            val djStatus: Long? = null,
//            @SerializedName("followed")
//            @Expose
//            val followed: Boolean? = null,
//            @SerializedName("mutual")
//            @Expose
//            val mutual: Boolean? = null,
//            @SerializedName("remarkName")
//            @Expose
//            val remarkName: Any? = null,
//            @SerializedName("avatarUrl")
//            @Expose
//            val avatarUrl: String? = null,
//            @SerializedName("backgroundUrl")
//            @Expose
//            val backgroundUrl: String? = null,
//            @SerializedName("nickname")
//            @Expose
//            val nickname: String? = null,
//            @SerializedName("avatarImgIdStr")
//            @Expose
//            val avatarImgIdStr: String? = null,
//            @SerializedName("backgroundImgIdStr")
//            @Expose
//            val backgroundImgIdStr: String? = null,
//            @SerializedName("description")
//            @Expose
//            val description: String? = null,
//            @SerializedName("userType")
//            @Expose
//            val userType: Long? = null,
//            @SerializedName("userId")
//            @Expose
//            val userId: Long? = null,
//            @SerializedName("accountStatus")
//            @Expose
//            val accountStatus: Long? = null,
//            @SerializedName("gender")
//            @Expose
//            val gender: Long? = null,
//            @SerializedName("signature")
//            @Expose
//            val signature: String? = null,
//            @SerializedName("authority")
//            @Expose
//            val authority: Long? = null
//
//    )
}