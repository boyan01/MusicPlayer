package tech.soit.quiet.ui.activity.cloud

import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.view.updatePadding
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_cloud_play_list_detail.*
import kotlinx.android.synthetic.main.item_cloud_play_list_detail_action.view.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.R
import tech.soit.quiet.model.po.NeteasePlayListDetail
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.viewmodel.CloudPlayListDetailViewModel
import tech.soit.quiet.ui.adapter.MusicListAdapter
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.blur
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

    private val viewModel by lazyViewModel<CloudPlayListDetailViewModel>()

    private lateinit var playlistToken: String

    private lateinit var detail: NeteasePlayListDetail

    private lateinit var adapter: MusicListAdapter

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
        toolbar.background = ColorDrawable(attrValue(R.attr.colorPrimary))
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
            toolbar.background.alpha = (alpha * 255).toInt()
        })
        setupActions()

        imageCreatorAvatar.outlineProvider = CircleOutlineProvider()
        imageCreatorAvatar.clipToOutline = true

        val playlistId = intent.getLongExtra("id", -1)
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

            GlobalScope.launch {
                val submit = ImageLoader.with(this@CloudPlayListDetailActivity).asBitmap()
                        .load(detail.getCoverUrl()).submit(appBarLayout.width, appBarLayout.height)
                val bitmap = submit.get()
                this@CloudPlayListDetailActivity.launch {
                    imageCover.setImageBitmap(bitmap)
                    val swatch = bitmap.generatePalette().await().getMuteSwatch()
                    toolbar.setBackgroundColor(swatch.rgb)
                    window.navigationBarColor = swatch.rgb
                    adapter.applyPrimaryColor(swatch.rgb)
                }

                //draw blur bitmap
                val blur = bitmap.blur(100, false)
                val crop = TransformationUtils.centerCrop(ImageLoader.get(this@CloudPlayListDetailActivity).bitmapPool,
                        blur, appBarLayout.width, appBarLayout.height)
                //draw a dark mask on the blur image, to make appbar layout background fit white action button
                Canvas(crop).drawColor(color(R.color.color_transparent_dark_secondary), PorterDuff.Mode.SRC_OVER)
                this@CloudPlayListDetailActivity.launch { appBarLayout.background = BitmapDrawable(resources, crop) }
            }
            ImageLoader.with(this@CloudPlayListDetailActivity).load(detail.getCoverUrl()).into(imageCover)

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