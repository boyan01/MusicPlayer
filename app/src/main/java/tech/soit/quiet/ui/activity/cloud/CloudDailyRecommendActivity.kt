package tech.soit.quiet.ui.activity.cloud

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cloud_daily_recommend.*
import kotlinx.coroutines.launch
import tech.soit.quiet.R
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.activity.cloud.adapter.DailyRecommendAdapter
import tech.soit.quiet.ui.adapter.MusicListAdapter
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.exception.NotLoginException
import tech.soit.quiet.utils.setEmpty
import tech.soit.quiet.utils.setLoading

/**
 * 每日推荐歌曲activity
 */
@LayoutId(R.layout.activity_cloud_daily_recommend)
@EnableBottomController
class CloudDailyRecommendActivity : BaseActivity() {

    private val neteaseRepository by lazyViewModel<NeteaseRepository>()

    private lateinit var adapter: MusicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = DailyRecommendAdapter()
        recyclerView.adapter = adapter

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        launch {
            val user = neteaseRepository.getLoginUser()
            if (user == null) {
                log { "未登录" }
                return@launch
            }
            adapter.setLoading()

            val list = try {
                neteaseRepository.recommendSongs()
            } catch (notLogin: NotLoginException) {
                //需要登录
                return@launch
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }

            if (list.isEmpty()) {
                adapter.setEmpty()
            } else {
                adapter.showList(list, false, false)
            }

        }
    }

}