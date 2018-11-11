package tech.soit.quiet.ui.adapter.viewholder

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.updatePadding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.header_play_list_detail.view.*
import kotlinx.android.synthetic.main.item_cloud_play_list_detail_action.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.ui.view.RoundRectOutlineProvider
import tech.soit.quiet.utils.component.ColorMaskTransformation
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.generatePalette
import tech.soit.quiet.utils.component.getMuteSwatch
import tech.soit.quiet.utils.component.support.color
import tech.soit.quiet.utils.component.support.px
import tech.soit.quiet.utils.event.PrimaryColorEvent

/**
 * 歌单详情
 */
class PlayListDetailViewHolder(itemView: View) : BaseViewHolder(itemView) {


    private var _playlist: PlayListDetail? = null

    fun bind(playList: PlayListDetail) = with(itemView) {
        if (_playlist == playList) {//已经显示了！
            return@with
        }
        _playlist = playList

        textPlayListTitle.text = playList.getName()
        ImageLoader.with(this).asBitmap()
                .load(playList.getCoverUrl())
                .into(object : BitmapImageViewTarget(imageCover) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(resource, transition)
                        GlobalScope.launch(Dispatchers.Main) {
                            val swatch = resource.generatePalette().await().getMuteSwatch()
                            EventBus.getDefault().post(PrimaryColorEvent(swatch.rgb))
                        }
                    }
                })
        ImageLoader.with(this).asBitmap().load(playList.getCoverUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transforms(CropTransformation(itemView.width, itemView.height),
                        BlurTransformation(100),
                        ColorMaskTransformation(color(R.color.color_transparent_dark_secondary)))
                .into(object : CustomViewTarget<View, Bitmap>(itemView), Transition.ViewAdapter {
                    override fun getCurrentDrawable(): Drawable? {
                        return itemView.background
                    }

                    override fun setDrawable(drawable: Drawable?) {
                        itemView.background = drawable
                    }


                    override fun onLoadFailed(errorDrawable: Drawable?) {

                    }

                    override fun onResourceCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        if (transition == null || !transition.transition(resource, this)) {
                            view.background = BitmapDrawable(resources, resource)
                        }
                    }
                })

        //creator
        ImageLoader.with(this).load(playList.getCreator().getAvatarUrl()).into(imageCreatorAvatar)
        textCreatorNickname.text = "%s>".format(playList.getCreator().getNickName())

        textPlayCount.text = playList.getPlayCount().toString()

    }

    init {

        itemView.apply {
            imageCreatorAvatar.outlineProvider = CircleOutlineProvider()
            imageCreatorAvatar.clipToOutline = true

            val coverOutline = RoundRectOutlineProvider(3.px.toFloat())
            imageCover.outlineProvider = coverOutline
            imageCover.clipToOutline = true

            maskImageCover.outlineProvider = coverOutline
            maskImageCover.clipToOutline = true
        }
        setupActions()
    }

    private fun setupActions() = with(itemView) {
        with(layoutComment) {
            imageIcon.setImageResource(R.drawable.ic_comment_black_24dp)
            text.setText(R.string.action_comment)
        }
        with(layoutShare) {
            imageIcon.setImageResource(R.drawable.ic_share_black_24dp)
            text.setText(R.string.action_share)
        }
        with(layoutDownload) {
            imageIcon.setImageResource(R.drawable.ic_file_download_black_24dp)
            text.setText(R.string.action_download)
        }
        with(layoutMultiSelect) {
            imageIcon.setImageResource(R.drawable.ic_select_all_black_24dp)
            text.setText(R.string.action_multi_select)
        }
    }

    fun addPaddingTop(insetTop: Int) {
        if (insetTop <= 0) {
            return
        }
        itemView.updatePadding(top = itemView.paddingTop + insetTop)
    }


}