package tech.summerly.quiet.commonlib.utils.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.GlideRequests
import tech.summerly.quiet.commonlib.utils.loadAndGet

fun ImageView.setImageUrl(url: String, round: Boolean = false) {
    GlideApp.with(this).load(url)
            .let {
                if (round) {
                    it.circleCrop()
                }
                it
            }
            .into(this)
}

fun View.setImageBackground(url: String) {
    GlideApp.with(this).load(url)
            .into(object : ViewTarget<View, Drawable>(this) {
                override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
                    background = resource
                }
            })
}


class PictureModel private constructor(internal val data: Any) {


    @Throws(Exception::class)
    suspend fun get(context: Any = LibModule,
                    circleCrop: Boolean = false,
                    width: Int = Target.SIZE_ORIGINAL,
                    height: Int = Target.SIZE_ORIGINAL): Bitmap {
        val glide = when (context) {
            is FragmentActivity -> GlideApp.with(context)
            is View -> GlideApp.with(context)
            is Fragment -> GlideApp.with(context)
            is Activity -> GlideApp.with(context)
            is Context -> GlideApp.with(context)
            else -> throw IllegalArgumentException("context error")
        }.asBitmap()
        if (circleCrop) {
            glide.circleCrop()
        }
        return glide.loadAndGet(data, width, height)!!
    }

    private fun getGlideRequests(context: Any): GlideRequests {
        return when (context) {
            is FragmentActivity -> GlideApp.with(context)
            is View -> GlideApp.with(context)
            is Fragment -> GlideApp.with(context)
            is Activity -> GlideApp.with(context)
            is Context -> GlideApp.with(context)
            else -> throw IllegalArgumentException("context error")
        }
    }

    fun load(context: Any) {
        val request = getGlideRequests(context).asBitmap()
        request.load(this.data)
                .into(object : BaseTarget<Bitmap>() {
                    override fun getSize(cb: SizeReadyCallback?) {

                    }

                    override fun removeCallback(cb: SizeReadyCallback?) {
                    }

                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                    }
                })
    }

    companion object {

        fun withUrl(url: String): PictureModel {
            return PictureModel(url)
        }

        fun with(any: Any): PictureModel {
            return PictureModel(any)
        }
    }

}