package tech.soit.quiet.repository.netease.source

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers

/**
 * 获取唯一标识符作缓存KEY
 */
class NeteaseGlideUrl(
        url: String,
        headers: Headers = Headers.DEFAULT
) : GlideUrl(url, headers) {

    override fun getCacheKey(): String {
        return super.getCacheKey().substringAfterLast('/')
    }
}