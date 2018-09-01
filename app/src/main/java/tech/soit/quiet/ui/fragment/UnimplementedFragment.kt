package tech.soit.quiet.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import tech.soit.quiet.ui.fragment.base.BaseFragment

@Suppress("unused")
/**
 *
 * fragment placeholder
 *
 * @author YangBin
 * @date 2018/8/12
 */
class UnimplementedFragment : BaseFragment() {

    override fun onCreateView2(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = FrameLayout(inflater.context)

        val text = TextView(inflater.context)
        @SuppressLint("SetTextI18n")
        text.text = "Unimplemented"

        layout.addView(text, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER))

        return layout
    }

}