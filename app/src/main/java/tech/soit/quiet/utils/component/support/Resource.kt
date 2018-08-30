package tech.soit.quiet.utils.component.support

/**
 * A generic class that holds a value with its loading status.
 *
 * @author : summer
 * @date : 18-8-29
 */
data class Resource<out T>(
        val status: Status,
        val data: T?,
        val message: String?
) {


    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

    /**
     * get data
     */
    fun requireData(): T {
        return data ?: throw NullPointerException("data is null")
    }

}

/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}