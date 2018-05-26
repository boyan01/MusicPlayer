package tech.summerly.quiet.player.ui

import android.os.Bundle
import android.view.View
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.base.BaseFragment

/**
 * 让 fragment 布局也支持 fitSystemWindows 属性
 */
open class InsetsFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? BaseActivity)?.requestApplyInserts()
    }

}