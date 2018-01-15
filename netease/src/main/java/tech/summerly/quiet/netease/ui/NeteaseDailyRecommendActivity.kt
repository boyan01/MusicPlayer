package tech.summerly.quiet.netease.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.launch
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.ctx
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi
import tech.summerly.quiet.netease.ui.items.*

/**
 * activity for daily recommend 30 songs
 */
@Route(path = "/netease/daily")
class NeteaseDailyRecommendActivity : BaseActivity() {

    private val view: RecyclerView by lazy {
        val recyclerView = RecyclerView(ctx)
        recyclerView.layoutManager = LinearLayoutManager(ctx)
        recyclerView
    }

    private val items = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
        view.adapter = MultiTypeAdapter(items).also {
            it.register(NeteaseDailyHeader::class.java, NeteaseDailyHeaderViewBinder())
            it.register(NeteaseMusicHeader::class.java, NeteaseMusicHeaderViewBinder())
            it.register(Music::class.java, NeteaseMusicItemViewBinder())
        }
        loadData()
    }

    private val loadExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    }

    private fun loadData() = launch(UI + loadExceptionHandler) {
        val songs = NeteaseCloudMusicApi().getDailyRecommend()
        items.add(NeteaseDailyHeader("12"))
        items.add(NeteaseMusicHeader(songs.size))
        items.addAll(songs)
        view.multiTypeAdapter.notifyDataSetChanged()
    }
}