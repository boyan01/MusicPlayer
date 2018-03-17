package tech.summerly.quiet.constraints

/*
 *
 * 各个activity的 router path
 *
 */

//本地歌曲
object Local {

    private const val LOCAL = "local"

    /**
     * LocalMainActivity router path
     */
    const val PATH_LOCAL_MAIN = "/$LOCAL/main"


}

//搜索
object Search {

    /**
     * search activity router path
     */
    const val ACTIVITY_SEARCH_MAIN = "/search/main"

}

object Netease {

    const val ACTIVITY_NETEASE_LOGIN = "/netease/login"


    const val ACTIVITY_NETASE_PLAYER = "/netease/player"

}

object Setting {

    const val ACTIVITY_SETTING_MAIN = "/setting/main"

}