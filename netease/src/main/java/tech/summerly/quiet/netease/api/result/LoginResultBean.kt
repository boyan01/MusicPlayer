package tech.summerly.quiet.netease.api.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/24
 * desc   :
 */
data class LoginResultBean(
        @SerializedName("loginType")
        @Expose
        val loginType: Long? = null,

        @SerializedName("code")
        @Expose
        val code: Int,


        @SerializedName("profile")
        @Expose
        val profile: Profile? = null

//        @SerializedName("account")
//        @Expose
//        var account: Account? = null,
//        @SerializedName("bindings")
//        @Expose
//        var bindings: List<Binding>? = null
) {

    data class Profile(

            @SerializedName("followed")
            @Expose
            val followed: Boolean? = null,

            @SerializedName("userId")
            @Expose
            val userId: Long,

            @SerializedName("nickname")
            @Expose
            val nickname: String,

            @SerializedName("avatarUrl")
            @Expose
            val avatarUrl: String? = null,

            @SerializedName("backgroundUrl")
            @Expose
            val backgroundUrl: String? = null

//        @SerializedName("avatarImgId")
//        @Expose
//        var avatarImgId: Long? = null,
//        @SerializedName("backgroundImgId")
//        @Expose
//        var backgroundImgId: Long? = null,
//        @SerializedName("detailDescription")
//        @Expose
//        var detailDescription: String? = null,
//
//        @SerializedName("djStatus")
//        @Expose
//        var djStatus: Long? = null,
//        @SerializedName("accountStatus")
//        @Expose
//        var accountStatus: Long? = null,
//        @SerializedName("defaultAvatar")
//        @Expose
//        var defaultAvatar: Boolean? = null,
//        @SerializedName("gender")
//        @Expose
//        var gender: Long? = null,
//        @SerializedName("birthday")
//        @Expose
//        var birthday: Long? = null,
//        @SerializedName("city")
//        @Expose
//        var city: Long? = null,
//        @SerializedName("province")
//        @Expose
//        var province: Long? = null,
//        @SerializedName("mutual")
//        @Expose
//        var mutual: Boolean? = null,
//        @SerializedName("remarkName")
//        @Expose
//        var remarkName: Any? = null,
//        @SerializedName("experts")
//        @Expose
//        var experts: Experts? = null,
//        @SerializedName("expertTags")
//        @Expose
//        var expertTags: Any? = null,
//        @SerializedName("userType")
//        @Expose
//        var userType: Long? = null,
//        @SerializedName("vipType")
//        @Expose
//        var vipType: Long? = null,
//        @SerializedName("authStatus")
//        @Expose
//        var authStatus: Long? = null,
//        @SerializedName("description")
//        @Expose
//        var description: String? = null,
//        @SerializedName("avatarImgIdStr")
//        @Expose
//        var avatarImgIdStr: String? = null,
//        @SerializedName("backgroundImgIdStr")
//        @Expose
//        var backgroundImgIdStr: String? = null,
//        @SerializedName("signature")
//        @Expose
//        var signature: String? = null,
//        @SerializedName("authority")
//        @Expose
//        var authority: Long? = null
    )

//data class Account(
//
//        @SerializedName("id")
//        @Expose
//        var id: Long? = null,
//        @SerializedName("userName")
//        @Expose
//        var userName: String? = null,
//        @SerializedName("type")
//        @Expose
//        var type: Long? = null,
//        @SerializedName("status")
//        @Expose
//        var status: Long? = null,
//        @SerializedName("whitelistAuthority")
//        @Expose
//        var whitelistAuthority: Long? = null,
//        @SerializedName("createTime")
//        @Expose
//        var createTime: Long? = null,
//        @SerializedName("salt")
//        @Expose
//        var salt: String? = null,
//        @SerializedName("tokenVersion")
//        @Expose
//        var tokenVersion: Long? = null,
//        @SerializedName("ban")
//        @Expose
//        var ban: Long? = null,
//        @SerializedName("baoyueVersion")
//        @Expose
//        var baoyueVersion: Long? = null,
//        @SerializedName("donateVersion")
//        @Expose
//        var donateVersion: Long? = null,
//        @SerializedName("vipType")
//        @Expose
//        var vipType: Long? = null,
//        @SerializedName("viptypeVersion")
//        @Expose
//        var viptypeVersion: Long? = null,
//        @SerializedName("anonimousUser")
//        @Expose
//        var anonimousUser: Boolean? = null
//
//)

//class Binding {
//
//    @SerializedName("expiresIn")
//    @Expose
//    var expiresIn: Long? = null
//    @SerializedName("refreshTime")
//    @Expose
//    var refreshTime: Long? = null
//    @SerializedName("url")
//    @Expose
//    var url: String? = null
//    @SerializedName("userId")
//    @Expose
//    var userId: Long? = null
//    @SerializedName("tokenJsonStr")
//    @Expose
//    var tokenJsonStr: String? = null
//    @SerializedName("expired")
//    @Expose
//    var expired: Boolean? = null
//    @SerializedName("id")
//    @Expose
//    var id: Long? = null
//    @SerializedName("type")
//    @Expose
//    var type: Long? = null
//
//}
}