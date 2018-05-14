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


    const val FRAGMENT_LOCAL_MAIN = "/$LOCAL/fragment/main"

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


    const val ACTIVITY_NETEASE_PLAYER = "/netease/player"


    const val ACTIVITY_NETEASE_MAIN = "/netease/main"

}

object Setting {

    const val ACTIVITY_SETTING_MAIN = "/setting/main"

}

object PlaylistDetail {

    const val PARAM_PLAYLIST_PROVIDER = "param_playlist_provider"

    /**
     * 歌单详情 activity
     *
     * 需要传入一个 PlaylistProivder，其 key 为 [PARAM_PLAYLIST_PROVIDER]
     */
    const val ACTIVITY_PLAYLIST_DETAIL = "/playlistdetail/main"

    const val ITEM_BINDER_MUSIC = "/items/music"

}

object Player {
    const val FRAGMENT_FM_PLAYER_NORMAL = "/player/normal_fm"
    const val FRAGMENT_FM_PLAYER_SIMPLE = "/player/simple_fm"
    const val FRAGMENT_MUSIC_PLAYER = "/player/music"
}