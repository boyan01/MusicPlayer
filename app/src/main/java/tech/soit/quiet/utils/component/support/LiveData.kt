package tech.soit.quiet.utils.component.support

import androidx.lifecycle.*


/**
 * @see Transformations.map
 */
fun <T, R> LiveData<T>.map(function: (T?) -> R?): LiveData<R> {
    return Transformations.map(this, function)
}

/**
 * the same as [map],but ignore [function] when received a null object
 */
fun <T, R> LiveData<T>.mapNonNull(function: (T) -> R): LiveData<R> {
    return map { t: T? ->
        t ?: return@map null
        return@map function(t)
    }
}


/**
 * @see Transformations.switchMap
 */
fun <T, R> LiveData<T>.switchMap(function: (T) -> LiveData<R>?): LiveData<R> {
    return Transformations.switchMap(this, function)
}


/**
 * observe LiveData but filter null change
 *
 * @see LiveData.observe
 */
fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner, Observer {
        if (it != null) {
            observer(it)
        }
    })
}


/**
 * create MutableLiveData with initial value
 */
fun <T> liveDataWith(initial: T): MutableLiveData<T> {
    val liveData = MutableLiveData<T>()
    liveData.postValue(initial)//use post to fit any thread
    return liveData
}