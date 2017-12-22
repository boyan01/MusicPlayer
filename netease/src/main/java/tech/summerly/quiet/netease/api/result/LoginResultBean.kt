package tech.summerly.quiet.netease.api.result


/**
 * author : SUMMERLY
 * time   : 2017/8/24
 * desc   :
 */
data class LoginResultBean(
        val loginType: Long? = null,

        val code: Int, //200表示登陆成功


        val profile: Profile? = null

//        var account: Account? = null,
//        var bindings: List<Binding>? = null
) {

    data class Profile(

            val followed: Boolean? = null,

            val userId: Long,

            val nickname: String,

            val avatarUrl: String? = null,

            val backgroundUrl: String? = null

//        var avatarImgId: Long? = null,
//        var backgroundImgId: Long? = null,
//        var detailDescription: String? = null,
//
//        var djStatus: Long? = null,
//        var accountStatus: Long? = null,
//        var defaultAvatar: Boolean? = null,
//        var gender: Long? = null,
//        var birthday: Long? = null,
//        var city: Long? = null,
//        var province: Long? = null,
//        var mutual: Boolean? = null,
//        var remarkName: Any? = null,
//        var experts: Experts? = null,
//        var expertTags: Any? = null,
//        var userType: Long? = null,
//        var vipType: Long? = null,
//        var authStatus: Long? = null,
//        var description: String? = null,
//        var avatarImgIdStr: String? = null,
//        var backgroundImgIdStr: String? = null,
//        var signature: String? = null,
//        var authority: Long? = null
    )

//data class Account(
//
//        var id: Long? = null,
//        var userName: String? = null,
//        var type: Long? = null,
//        var status: Long? = null,
//        var whitelistAuthority: Long? = null,
//        var createTime: Long? = null,
//        var salt: String? = null,
//        var tokenVersion: Long? = null,
//        var ban: Long? = null,
//        var baoyueVersion: Long? = null,
//        var donateVersion: Long? = null,
//        var vipType: Long? = null,
//        var viptypeVersion: Long? = null,
//        var anonimousUser: Boolean? = null
//
//)

//class Binding {
//
//    var expiresIn: Long? = null
//    var refreshTime: Long? = null
//    var url: String? = null
//    var userId: Long? = null
//    var tokenJsonStr: String? = null
//    var expired: Boolean? = null
//    var id: Long? = null
//    var type: Long? = null
//
//}
}