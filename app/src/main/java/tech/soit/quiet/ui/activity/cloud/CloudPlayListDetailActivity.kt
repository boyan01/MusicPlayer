package tech.soit.quiet.ui.activity.cloud

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EdgeEffect
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_cloud_play_list_detail.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.viewmodel.CloudPlayListDetailViewModel
import tech.soit.quiet.ui.adapter.MusicListAdapter2
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.dimen
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.event.PrimaryColorEvent
import tech.soit.quiet.utils.event.WindowInsetsEvent

@LayoutId(R.layout.activity_cloud_play_list_detail)
@EnableBottomController
class CloudPlayListDetailActivity : BaseActivity() {

    companion object {

        const val PARAM_ID = "id"

    }

    private val viewModel by lazyViewModel<CloudPlayListDetailViewModel>()

    private lateinit var playlistToken: String

    private lateinit var detail: PlayListDetail

    private lateinit var adapter: MusicListAdapter2

    /**
     * toolbar 背景色，需要随着歌单封面的不同而换成相应的颜色
     */
    private lateinit var toolbarBackground: ColorDrawable

    /**
     * 标题栏高度
     */
    private var headerHeight = (dimen(R.dimen.height_playlist_detail_cover) +
            dimen(R.dimen.height_playlist_detail_nav)).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        layoutRoot.setOnApplyWindowInsetsListener { _, insets ->
            if (insets.systemWindowInsetTop != 0) {
                toolbar.updatePadding(top = insets.systemWindowInsetTop)
                EventBus.getDefault().post(WindowInsetsEvent(insets))
            }
            return@setOnApplyWindowInsetsListener insets.consumeSystemWindowInsets()
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbarBackground = ColorDrawable(attrValue(R.attr.colorPrimary))
        toolbar.background = toolbarBackground
        toolbar.bringToFront()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var offset: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                offset += dy

                onHeaderOffset(offset)
            }

            fun onHeaderOffset(offset: Int) {
                val alpha = offset / headerHeight.toFloat()

                val title = if (alpha > 0.5) {
                    detail.getName()
                } else {
                    string(R.string.title_play_list)
                }
                if (toolbar.title != title) {
                    toolbar.title = title
                }
                toolbarBackground.alpha = (alpha.coerceIn(0F, 1F) * 255).toInt()
            }

        })


        val playlistId = intent.getLongExtra(PARAM_ID, -1)
        if (playlistId == -1L) {
            error("need playlist id")
        }

        playlistToken = "netease_$playlistId"

        adapter = MusicListAdapter2()
        recyclerView.adapter = adapter

        launch {
            val detail = viewModel.loadData(playlistId)
                    ?: //TODO
                    return@launch
            this@CloudPlayListDetailActivity.detail = detail

            //show music list
            val user = viewModel.getLoginUser()
            adapter.showList(user, detail)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPrimaryColorChanged(e: PrimaryColorEvent) {
        val color = e.color
        val alpha = toolbarBackground.alpha
        toolbarBackground.color = color
        toolbarBackground.alpha = alpha
        window.navigationBarColor = color
        adapter.applyPrimaryColor(color)

        //设置 recycler view 边界效果的颜色
        recyclerView.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
            override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
                val effect = super.createEdgeEffect(view, direction)
                effect.color = color
                return effect
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}