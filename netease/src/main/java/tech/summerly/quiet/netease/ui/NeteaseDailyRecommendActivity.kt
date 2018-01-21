package tech.summerly.quiet.netease.ui

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.netease_activity_daily_recommend.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.utils.alert
import tech.summerly.quiet.commonlib.utils.multiTypeAdapter
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.api.NeteaseCloudMusicApi
import tech.summerly.quiet.netease.ui.items.*
import tech.summerly.quiet.netease.utils.isLogin

/**
 * activity for daily recommend 30 songs
 */
@Route(path = "/netease/daily")
class NeteaseDailyRecommendActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    companion object {
        private val REQUEST_LOGIN = 101
    }

    private val items = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_daily_recommend)
        recyclerList.adapter = MultiTypeAdapter(items).also {
            it.register(NeteaseDailyHeader::class.java, NeteaseDailyHeaderViewBinder())
            it.register(NeteaseMusicHeader::class.java, NeteaseMusicHeaderViewBinder())
            it.register(Music::class.java, NeteaseMusicItemViewBinder(this::onMusicClick))
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        loadData()
    }

    private fun loadData() = asyncUI {
        //check if login
        if (!isLogin()) {
            if (alert(message = getString(R.string.netease_alert_need_login),
                            negative = getString(R.string.netease_action_give_up))) {
                ARouter.getInstance().build("/netease/login")
                        .navigation(this@NeteaseDailyRecommendActivity, REQUEST_LOGIN)
            } else {
                finish()
            }
            return@asyncUI
        }
        val songs = NeteaseCloudMusicApi().getDailyRecommend()
        items.add(NeteaseDailyHeader("12"))
        items.add(NeteaseMusicHeader(songs.size))
        items.addAll(songs)
        recyclerList.multiTypeAdapter.notifyDataSetChanged()
    }

    private fun onMusicClick(music: Music) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //do not need to check if is login success,because loadData method will check it
        if (requestCode == REQUEST_LOGIN) {
            loadData()
        }
    }
}