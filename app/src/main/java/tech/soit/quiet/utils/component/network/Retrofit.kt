package tech.soit.quiet.utils.component.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @see Call.enqueue
 */
fun <T> Call<T>.enqueue(callBack: ICallBack<T>.() -> Unit) {
    val instance = ICallBack<T>()
    instance.callBack()
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            instance.onFailure?.invoke(call, t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            instance.onResponse?.invoke(call, response)
        }
    })
}

class ICallBack<T> {

    var onResponse: ((call: Call<T>, response: Response<T>) -> Unit)? = null

    var onFailure: ((call: Call<T>, t: Throwable) -> Unit)? = null

    fun onResponse(onResponse: (call: Call<T>, response: Response<T>) -> Unit) {
        this.onResponse = onResponse
    }

    fun onFailure(onFailure: (call: Call<T>, t: Throwable) -> Unit) {
        this.onFailure = onFailure
    }


}
