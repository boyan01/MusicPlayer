package debug

import android.os.Bundle
import android.support.transition.TransitionManager
import androidx.view.isGone
import kotlinx.android.synthetic.main.search_item_chips.*
import kotlinx.coroutines.experimental.delay
import org.jetbrains.anko.forEachChild
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.search.R
import tech.summerly.quiet.search.fragments.items.generateChipItem

/**
 * Created by summer on 18-3-19
 */
class DebugActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_item_chips)
    }

    override fun onResume() {
        super.onResume()
        asyncUI {
            delay(200)
            chipLayout.forEachChild {
                it.setOnClickListener {
                    TransitionManager.beginDelayedTransition(chipLayout)
                    it.isGone = true
                }
            }
        }

        (0..10).forEach {
            chipLayout.addView(getView())
        }
    }


    private fun getView() = generateChipItem("你好", chipLayout)
}