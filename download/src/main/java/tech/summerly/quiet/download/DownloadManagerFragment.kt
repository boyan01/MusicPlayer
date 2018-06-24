package tech.summerly.quiet.download

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Route
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.commonlib.utils.color
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.constraints.Download
import tech.summerly.quiet.download.binders.DownloadItem
import tech.summerly.quiet.download.binders.DownloadingItemBinder

@Route(path = Download.DOWNLOAD_MAIN)
class DownloadManagerFragment : BaseFragment() {

    companion object {
        fun newInstance() = DownloadManagerFragment()
    }

    private lateinit var viewModel: DownloadManagerViewModel

    val binder = DownloadingItemBinder()

    lateinit var viewHolder: ViewHolder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val context = ContextThemeWrapper(requireContext(), R.style.AppTheme)
        val layout = FrameLayout(context, null, 0)
        layout.setBackgroundColor(color(R.color.background))
        viewHolder = binder.onCreateViewHolder(layout)
        layout.addView(viewHolder.itemView)
        layout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binder.onBindViewHolder(viewHolder, DownloadItem())
            }
        })
        return layout
//        return inflater.inflate(R.layout.download_manager_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DownloadManagerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
