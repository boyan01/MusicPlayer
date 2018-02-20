package tech.summerly.quiet.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.search_activity_main.*
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.fragments.BottomControllerFragment
import tech.summerly.quiet.commonlib.utils.gone
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.visible
import tech.summerly.quiet.search.fragments.SearchResultsFragment
import tech.summerly.quiet.search.utils.inTransaction

/**
 * Created by summer on 18-2-17
 */
@Route(path = "/search/main")
class SearchMainActivity : BaseActivity(), BottomControllerFragment.BottomControllerContainer {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity_main)
        initView()
    }

    private val voiceClickListener = { _: View ->
        toast("the function is not implemented")
    }

    private val queryClickListener = { _: View ->
        startQuery()
    }

    private fun initView() {
        buttonBack.setOnClickListener {
            onBackPressed()
        }
        buttonIndicator.setOnClickListener(voiceClickListener)
        buttonClear.setOnClickListener {
            editQuery.setText("")
        }
        editQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString()
                if (text == null || text.isEmpty()) {
                    buttonIndicator.setImageResource(R.drawable.search_ic_keyboard_voice_black_24dp)
                    buttonIndicator.setOnClickListener(voiceClickListener)
                    buttonClear.gone()
                } else {
                    buttonIndicator.setImageResource(R.drawable.search_ic_search_black_24dp)
                    buttonIndicator.setOnClickListener(queryClickListener)
                    buttonClear.visible()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        editQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_NONE) {
                startQuery()
            }
            false
        }
    }

    private fun startQuery(text: String = editQuery.text.toString().trim()) {
        log { "查询:$text" }
        if (text.isEmpty()) {
            //do nothing
            return
        }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editQuery.windowToken, 0)
        val fragment = SearchResultsFragment.newInstance(text)
        supportFragmentManager.inTransaction {
            replace(R.id.layoutContainer, fragment)
        }
    }
}