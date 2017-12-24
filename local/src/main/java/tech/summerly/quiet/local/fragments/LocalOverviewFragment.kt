package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.summerly.quiet.commonlib.base.BaseFragment

/**
 * Created by summer on 17-12-17
 * provide an overview for LocalMusic : LocalFavMusic , LocalMusicMonitor
 */
class LocalOverviewFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "OverView"
            gravity = Gravity.CENTER
        }
    }

}