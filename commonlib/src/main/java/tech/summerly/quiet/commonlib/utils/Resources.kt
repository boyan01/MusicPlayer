package tech.summerly.quiet.commonlib.utils

import android.content.Context
import android.support.annotation.ColorRes

/**
 * Created by summer on 17-12-21
 */
fun Context.color(@ColorRes id: Int) = resources.getColor(id)