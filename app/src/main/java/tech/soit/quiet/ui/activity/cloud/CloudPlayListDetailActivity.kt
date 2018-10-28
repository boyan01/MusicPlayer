package tech.soit.quiet.ui.activity.cloud

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.view.updatePadding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import kotlinx.android.synthetic.main.activity_cloud_play_list_detail.*
import kotlinx.android.synthetic.main.item_cloud_play_list_detail_action.view.*
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.viewmodel.CloudPlayListDetailViewModel
import tech.soit.quiet.ui.adapter.MusicListAdapter
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ColorMaskTransformation
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.generatePalette
import tech.soit.quiet.utils.component.getMuteSwatch
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.color
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.setEmpty
import tech.soit.quiet.utils.setLoading

@LayoutId(R.layout.activity_cloud_play_list_detail)
@EnableBottomController
class CloudPlayListDetailActivity : BaseActivity() {

    companion object {


        const val PARAM_ID = "id"

    }

    private val viewModel by lazyViewModel<CloudPlayListDetailViewModel>()

    private lateinit var playlistToken: String

    private lateinit var detail: PlayListDetail

    private lateinit var adapter: MusicListAdapter

    private lateinit var toolbarBackground: ColorDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRoot.setOnApplyWindowInsetsListener { _, insets ->
            if (insets.systemWindowInsetTop != 0) {
                appBarLayout.updatePadding(top = appBarLayout.paddingTop + insets.systemWindowInsetTop)
                toolbar.updatePadding(top = insets.systemWindowInsetTop)
            }
            return@setOnApplyWindowInsetsListener insets.consumeSystemWindowInsets()
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbarBackground = ColorDrawable(attrValue(R.attr.colorPrimary))
        toolbar.background = toolbarBackground
        toolbar.bringToFront()

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            val alpha = -offset.toFloat() / appBarLayout.totalScrollRange
            val title = if (alpha > 0.5) {
                textPlayListTitle.text
            } else {
                string(R.string.title_play_list)
            }
            if (toolbar.title != title) {
                toolbar.title = title
            }
            toolbarBackground.alpha = (alpha * 255).toInt()
        })
        setupActions()

        imageCreatorAvatar.outlineProvider = CircleOutlineProvider()
        imageCreatorAvatar.clipToOutline = true

        val playlistId = intent.getLongExtra(PARAM_ID, -1)
        if (playlistId == -1L) {
            error("need playlist id")
        }

        playlistToken = "netease_$playlistId"


        adapter = MusicListAdapter(playlistToken)
        recyclerView.adapter = adapter

        launch {
            adapter.setLoading()
            val detail = viewModel.loadData(playlistId)
            if (detail == null) {
                adapter.setEmpty()
                return@launch
            }
            this@CloudPlayListDetailActivity.detail = detail
            textPlayListTitle.text = detail.getName()

            ImageLoader.with(this@CloudPlayListDetailActivity).asBitmap()
                    .load(detail.getCoverUrl())
                    .into(object : BitmapImageViewTarget(imageCover) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {

                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            super.onResourceReady(resource, transition)
                            this@CloudPlayListDetailActivity.launch {
                                val swatch = resource.generatePalette().await().getMuteSwatch()
                                val alpha = toolbarBackground.alpha
                                toolbarBackground.color = swatch.rgb
                                toolbarBackground.alpha = alpha
                                window.navigationBarColor = swatch.rgb
                                adapter.applyPrimaryColor(swatch.rgb)
                            }
                        }
                    })
            ImageLoader.with(this@CloudPlayListDetailActivity).asBitmap().load(detail.getCoverUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transforms(CropTransformation(appBarLayout.width, appBarLayout.height),
                            BlurTransformation(100),
                            ColorMaskTransformation(color(R.color.color_transparent_dark_secondary)))
                    .into(object : CustomViewTarget<AppBarLayout, Bitmap>(appBarLayout), Transition.ViewAdapter {
                        override fun getCurrentDrawable(): Drawable? {
                            return appBarLayout.background
                        }

                        override fun setDrawable(drawable: Drawable?) {
                            appBarLayout.background = drawable
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
            ImageLoader.with(this@CloudPlayListDetailActivity).load(detail.getCreator().getAvatarUrl()).into(imageCreatorAvatar)
            textCreatorNickname.text = "%s>".format(detail.getCreator().getNickName())

            textPlayCount.text = detail.getPlayCount().toString()

            //show music list
            val user = viewModel.getLoginUser()
            val isShowCollectionButton = user == null || user.getId() == detail.getCreator().getId()
            val isSubscribed = user != null && user.getId() != detail.getCreator().getId()
                    && detail.isSubscribed()
            adapter.showList(detail.getTracks(), isShowCollectionButton, isSubscribed)
        }
    }


    private fun setupActions() {
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

}