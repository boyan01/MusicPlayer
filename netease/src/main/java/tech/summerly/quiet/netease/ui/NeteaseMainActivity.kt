package tech.summerly.quiet.netease.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.netease_activity_main.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.items.CommonItemA
import tech.summerly.quiet.commonlib.items.CommonItemAViewBinder
import tech.summerly.quiet.netease.R

/**
 * Created by summer on 17-12-30
 */
class NeteaseMainActivity : BaseActivity() {

    private val navItems = listOf(
            CommonItemA(R.string.netease_nav_title_local, R.drawable.netease_ic_arrow_back_white_24dp)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_main)
        recycler.adapter = MultiTypeAdapter(mutableListOf<Any>()).also {
            it.register(CommonItemA::class.java, CommonItemAViewBinder(this::onNavItemClicked, R.color.netease_color_primary))
        }
    }

    private fun onNavItemClicked(item: CommonItemA) {

    }

}