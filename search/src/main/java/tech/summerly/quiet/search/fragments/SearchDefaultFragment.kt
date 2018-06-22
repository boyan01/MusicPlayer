package tech.summerly.quiet.search.fragments

import android.os.Bundle
import android.support.design.chip.Chip
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_content_default.view.*
import kotlinx.android.synthetic.main.search_fragment_default.view.*
import tech.summerly.quiet.commonlib.base.BaseFragment
import tech.summerly.quiet.search.R

/**
 * Created by summer on 18-3-6
 */
internal class SearchDefaultFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.search_fragment_default, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(view) {
        super.onViewCreated(view, savedInstanceState)
        bindHotKeywords(view.container)
        Unit
    }

    override fun onStart() {
        super.onStart()
    }

    private fun bindHotKeywords(container: ViewGroup) {
        val keywords = listOf("慢慢喜欢你", "等你下课", "五月天", "方大同", "9420", "Welcome To New York")
        if (keywords.isEmpty()) {
            return
        }
        val view = LayoutInflater.from(container.context).inflate(R.layout.search_content_default, container)
        keywords.forEach { keyword ->
            val chip = Chip(container.context)
            with(chip) {
                chipText = keyword
                setChipBackgroundColorResource(R.color.background)
                setChipStrokeColorResource(R.color.search_chip_colors)
                chipStrokeWidth = 1f
            }
            chip.setOnClickListener {
                (parentFragment as SearchMainFragment?)?.search(keyword)
            }
            view.chipLayout.addView(chip)
        }
    }

}