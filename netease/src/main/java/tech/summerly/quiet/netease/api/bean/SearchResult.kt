package tech.summerly.quiet.netease.api.bean

import tech.summerly.quiet.commonlib.bean.Music

/**
 * author : yangbin10
 * date   : 2017/12/21
 */
data class MusicSearchResult(val keyword: String,
                             val musics: List<Music>,
                             val total: Int,
                             val offset: Int)