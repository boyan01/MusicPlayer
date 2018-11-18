package tech.soit.quiet.ui.activity

import android.os.Bundle
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_latest_play_list.*
import tech.soit.quiet.R
import tech.soit.quiet.repository.LatestPlayingRepository
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.adapter.LatestPlayListAdapter
import tech.soit.quiet.utils.annotation.EnableBottomController
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.dimen

@LayoutId(R.layout.activity_latest_play_list)
@EnableBottomController
class LatestPlayListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRoot.setOnApplyWindowInsetsListener { _, insets ->
            toolbar.updatePadding(top = toolbar.paddingTop + insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val adapter = LatestPlayListAdapter()
        recyclerView.adapter = adapter

        LatestPlayingRepository.getInstance().getLatestPlayMusic().observe(this, Observer { musics ->
            adapter.showLatestPlayList(musics)
        })

        layoutRoot.doOnLayout {
            adapter.placeholderHeight = layoutRoot.height -
                    (toolbar.height + dimen(R.dimen.height_header_music_list).toInt())
        }

    }

}