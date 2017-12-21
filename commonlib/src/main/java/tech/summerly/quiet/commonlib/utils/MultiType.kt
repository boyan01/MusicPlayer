package tech.summerly.quiet.commonlib.utils

import android.support.v7.widget.RecyclerView
import me.drakeet.multitype.MultiTypeAdapter

/**
 * Created by summer on 17-12-17
 */
val RecyclerView.multiTypeAdapter: MultiTypeAdapter
    get() = adapter as? MultiTypeAdapter
            ?: throw IllegalStateException("must set multiType adapter first!")