package tech.summerly.quiet.commonlib.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.base.BaseFragment

/**
 * Created by summer on 18-2-20
 */
class UnimplementedFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_unimplemented, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
    }

}