package tech.summerly.quiet.data.netease.result

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/24
 * desc   : 签到成功 {"point":3,"code":200}
 *          重复签到 : {"code":-2,"msg":"重复签到"}
 */
data class DailySignResultBean(
        val code: Int,
        val point: Int?, //经验点数
        val msg: String?
)