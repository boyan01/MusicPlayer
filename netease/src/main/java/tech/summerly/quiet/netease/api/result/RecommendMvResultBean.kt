package tech.summerly.quiet.netease.api.result


/**
 * author : SUMMERLY
 * time   : 2017/8/25
 * desc   :
 */
data class RecommendMvResultBean(
        val code: Long,
        val category: Long? = null,
        val result: List<Result>? = null
) {
    data class Result(

            val id: Long,

            val name: String,

            val copywriter: String? = null,

            val picUrl: String? = null,

            val duration: Long? = null,
            val playCount: Long? = null,
            val artistName: String? = null,
            val artistId: Long? = null

    )
}
