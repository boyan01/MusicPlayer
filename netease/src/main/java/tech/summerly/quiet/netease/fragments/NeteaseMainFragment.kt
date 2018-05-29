package tech.summerly.quiet.netease.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.netease_fragment_main.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.netease.R
import tech.summerly.quiet.netease.adapters.MainRecyclerAdapter

class NeteaseMainFragment : BaseFragment() {


    private val adapter by lazy { MainRecyclerAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.netease_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        recycler.adapter = adapter
        adapter.show()
    }

}