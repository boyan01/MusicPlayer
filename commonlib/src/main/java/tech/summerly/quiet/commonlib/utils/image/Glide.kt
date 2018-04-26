package tech.summerly.quiet.commonlib.utils.image

import android.widget.ImageView
import tech.summerly.quiet.commonlib.utils.GlideApp

fun ImageView.setImageUrl(url: String) {
    GlideApp.with(this).load(url).into(this)
}