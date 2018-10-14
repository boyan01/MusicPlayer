package tech.soit.quiet.ui.activity.cloud

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_cloud_play_list_detail.*
import kotlinx.android.synthetic.main.item_cloud_play_list_detail_action.view.*
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.model.po.NeteasePlayListDetail
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.ui.activity.MusicPlayerActivity
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.viewmodel.CloudPlayListDetailViewModel
import tech.soit.quiet.ui.item.ItemMusicListHeader
import tech.soit.quiet.ui.item.MusicItemViewBinder
import tech.soit.quiet.ui.item.MusicListHeaderViewBinder
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.*
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.blur
import tech.soit.quiet.utils.component.generatePalette
import tech.soit.quiet.utils.component.getMuteSwatch
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.color

@LayoutId(R.layout.activity_cloud_play_list_detail)
@EnableBottomController
class CloudPlayListDetailActivity : BaseActivity() {

    private val viewModel by lazyViewModel<CloudPlayListDetailViewModel>()

    private lateinit var playlistToken: String

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
            if (alpha > 0.5) {
                toolbar.title = textPlayListTitle.text
            } else {
                toolbar.setTitle(R.string.title_play_list)
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

        val musicItemViewBinder = MusicItemViewBinder(this, playlistToken, { _, music ->
            val playlist = MusicPlayerManager.musicPlayer.playlist
            if (playlist.token == playlistToken && playlist.current == music) {
                //if current is playing.. open player activity
                startActivity(Intent(this, MusicPlayerActivity::class.java))
            } else {
                MusicPlayerManager.play(playlistToken, music, detail.getTracks())
            }
        })
        adapter = MultiTypeAdapter()
                .withEmptyBinder()
                .withLoadingBinder()
                .withBinder(musicItemViewBinder)
                .withBinder(MusicListHeaderViewBinder(
                        onClicked = {
                            MusicPlayerManager.play(playlistToken, detail.getTracks()[0], detail.getTracks())
                        },
                        onCollectionClicked = {
                            //TODO
                        }))
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
                    musicItemViewBinder.colorIndicator = swatch.rgb
                }

                //draw blur bitmap
                val blur = bitmap.blur(100, false)

                val bg = Bitmap.createBitmap(appBarLayout.width, appBarLayout.height, Bitmap.Config.RGB_565)
                val canvas = Canvas(bg)

                //calculate matrix to make bg center corp
                val matrix = Matrix()
                val scale: Float
                var dx = 0F
                var dy = 0F
                if (blur.width * bg.height > blur.height * bg.width) {
                    scale = bg.height.toFloat() / blur.height
                    dx = (bg.width - blur.width * scale) / 2
                } else {
                    scale = bg.width.toFloat() / blur.width
                    dy = (bg.height - blur.height * scale) / 2
                }
                matrix.postScale(scale, scale)
                matrix.postTranslate(dx, dy)

                canvas.drawBitmap(blur, matrix, Paint())
                blur.recycle()
                canvas.drawColor(color(R.color.color_transparent_dark_secondary), PorterDuff.Mode.SRC_OVER)
                this@CloudPlayListDetailActivity.launch { appBarLayout.background = BitmapDrawable(resources, bg) }
            }
            ImageLoader.with(this@CloudPlayListDetailActivity).load(detail.getCoverUrl()).into(imageCover)

            //creator
            ImageLoader.with(this@CloudPlayListDetailActivity).load(detail.getCreator().getAvatarUrl()).into(imageCreatorAvatar)
            textCreatorNickname.text = "%s>".format(detail.getCreator().getNickName())

            //music list header info
            val user = viewModel.getLoginUser()
            val isShowCollectionButton = user == null || user.getId() == detail.getCreator().getId()
            val isSubscribe = user != null && user.getId() != detail.getCreator().getId()
                    && detail.isSubscribed()
            val header = ItemMusicListHeader(detail.getTracks().size,
                    isShowCollectionButton, isSubscribe)

            val items = ArrayList<Any>()
            items.add(header)
            items.addAll(detail.getTracks())
            adapter.submit(items)

            subscribeData()
        }
    }

    private fun subscribeData() {

        MusicPlayerManager.playingMusic.observe(this, object : Observer<Music?> {

            private var current: Music? = null

            override fun onChanged(playing: Music?) {
                if (MusicPlayerManager.musicPlayer.playlist.token != playlistToken) {
                    return
                }
                val old = adapter.items.indexOf(current)
                val new = adapter.items.indexOf(playing)
                if (old != -1) {
                    setIndicator(old, false)
                }
                if (new != -1) {
                    setIndicator(new, true)
                }
                current = playing
            }

            private fun setIndicator(adapterPosition: Int, show: Boolean) {
                recyclerView
                        .findViewHolderForAdapterPosition(adapterPosition)
                        ?.itemView
                        ?.findViewById<View>(R.id.indicatorPlaying)
                        ?.isGone = show
            }

        })
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