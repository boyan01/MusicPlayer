package tech.summerly.quiet.player.ui

import android.os.Bundle
import android.view.View
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.base.BaseFragment

open class InsetsFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? BaseActivity)?.requestApplyInserts()
    }

}