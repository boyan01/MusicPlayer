package tech.summerly.quiet.commonlib.objects

/**
 * Created by summer on 18-2-20
 * portion of a list
 * @param total : the truly size of list
 * @param offset: the offset of this portion in list
 */
class PortionList<T : Any>(
        private val data: MutableList<T>,
        var total: Int,
        var offset: Int
) : List<T> by data {

    fun add(t: T) {
        data.add(t)
    }

    fun mix(new: PortionList<out T>) {
        //not check legality because do not need to...
        if (total == 0) {
            total = new.total
        }
        data.addAll(new.data)
    }


    fun <R : Any> map(transform: (T) -> R): PortionList<R> {
        val new = ArrayList<R>()
        data.mapTo(new, transform)
        return PortionList(new, total, offset)
    }

    fun clear() {
        data.clear()
        offset = 0
    }

}