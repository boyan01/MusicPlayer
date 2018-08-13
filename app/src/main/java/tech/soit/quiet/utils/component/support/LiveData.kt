package tech.soit.quiet.utils.component.support

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations


/**
 * @see Transformations.map
 */
fun <T, R> LiveData<T>.map(function: (T) -> R): LiveData<R> {
    return Transformations.map(this, function)
}


/**
 * @see Transformations.switchMap
 */
fun <T, R> LiveData<T>.swithMap(function: (T) -> LiveData<R>): LiveData<R> {
    return Transformations.switchMap(this, function)
}