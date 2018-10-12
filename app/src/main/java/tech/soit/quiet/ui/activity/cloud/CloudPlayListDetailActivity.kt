package tech.soit.quiet.ui.activity.cloud

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_cloud_play_list_detail.*
import kotlinx.android.synthetic.main.item_cloud_play_list_detail_action.view.*
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.po.NeteasePlayListDetail
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.viewmodel.CloudPlayListDetailViewModel
import tech.soit.quiet.ui.item.MusicItemViewBinder
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.*
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.QuietViewModelProvider
import tech.soit.quiet.utils.component.support.attrValue

@LayoutId(R.layout.activity_cloud_play_list_detail)
class CloudPlayListDetailActivity : BaseActivity() {

    private val viewModel by lazyViewModel<CloudPlayListDetailViewModel>()

    init {
        viewModelFactory = object : QuietViewModelProvider() {
            override fun createViewModel(modelClass: Class<ViewModel>): ViewModel {
                if (modelClass == CloudPlayListDetailViewModel::class.java) {
                    return CloudPlayListDetailViewModel()
                }
                return super.createViewModel(modelClass)
            }
        }
    }

    private lateinit var detail: NeteasePlayListDetail

    private lateinit var adapter: MultiTypeAdapter

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
            toolbar.background.alpha = (alpha * 255).toInt()
        })
        setupActions()

        imageCreatorAvatar.outlineProvider = CircleOutlineProvider()
        imageCreatorAvatar.clipToOutline = true

        val playlistId = intent.getLongExtra("id", -1)
        if (playlistId == -1L) {
            error("need playlist id")
        }

        adapter = MultiTypeAdapter()
                .withEmptyBinder()
                .withLoadingBinder()
                .withBinder(MusicItemViewBinder("netease_$playlistId", { view, music -> }))
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
            ImageLoader.with(this@CloudPlayListDetailActivity).load(detail.getCoverUrl()).into(imageCover)

            //creator
            ImageLoader.with(this@CloudPlayListDetailActivity).load(detail.getCreator().getAvatarUrl()).into(imageCreatorAvatar)
            textCreatorNickname.text = detail.getCreator().getNickName()

            adapter.submit(detail.getTracks())
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