package tech.summerly.quiet.netease.api.result

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/23
 * desc   :
 */
data class LyricResultBean(
        val lrc: LrcBean?,
        val klyric: LrcBean?,
        val tlyric: LrcBean?,
        val code: Int
//                           val sgc: Boolean,
//                           val sfy: Boolean,
//                           val qfy: Boolean
) {
    data class LrcBean(val version: Int,
                                val lyric: String?)
}

