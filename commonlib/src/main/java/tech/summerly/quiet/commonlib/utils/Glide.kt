package tech.summerly.quiet.commonlib.utils

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.module.AppGlideModule
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.bean.Music
import java.net.URL

/**
 * author : yangbin10
 * date   : 2017/12/27
 */

@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setLogLevel(Log.VERBOSE)
        super.applyOptions(context, builder)
    }
}

class PictureUrl : GlideUrl {
    constructor(url: URL?) : super(url)
    constructor(url: String?) : super(url)
    constructor(url: URL?, headers: Headers?) : super(url, headers)
    constructor(url: String?, headers: Headers?) : super(url, headers)

    override fun getCacheKey(): String {
        val url = toStringUrl()
        if (url.startsWith("http://p1.music.126.net")) {
            return url.substringAfterLast("/")
        }
        return super.getCacheKey()
    }

}

private fun String?.toPictureUrl(): Any = PictureUrl(this)

fun Music.getPictureUrl(): Any {
    if (picUri == null) {
        return R.drawable.common_ic_favorite_border_red_24dp
    }
    if (picUri.startsWith("file:",true)){
        return picUri
    }
    return picUri.toPictureUrl()
}

fun Artist.getPictureUrl(): Any = this.picUri.toPictureUrl()

fun Album.getPictureUrl(): Any = this.picUri.toPictureUrl()