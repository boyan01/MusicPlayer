package tech.summerly.quiet.commonlib.utils.support

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.template.IProvider
import tech.summerly.quiet.commonlib.utils.log
import kotlin.reflect.KClass

open class TypedAdapter constructor(list: List<Any>) : RecyclerView.Adapter<ViewHolder>() {


    constructor() : this(emptyList())

    private var items: List<Any> = list

    private val pool = TypedBinderPool()

    val list: List<Any> get() = items.toList()

    fun <T : Any> withBinder(klass: KClass<T>, binder: TypedBinder<T>): TypedAdapter {
        pool.register(klass, binder)
        binder.attachAdapter(this)
        return this
    }

    fun setList(list: List<Any>, notify: Boolean = true) {
        this.items = list
        if (notify) {
            notifyDataSetChanged()
        }
    }

    fun submit(list: List<Any>) {
        setList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return pool.key(items[position]::class)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binder = pool.getBinder(viewType)
        return binder.onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        @Suppress("UNCHECKED_CAST")
        val binder = pool.getBinder(getItemViewType(position)) as TypedBinder<Any>
        binder.onBindViewHolder(holder, items[position])
    }

    fun getItem(adapterPosition: Int): Any {
        return items[adapterPosition]
    }

}

//type-binder pool to handle type and binder relationship
class TypedBinderPool {

    companion object {
        private const val START = 0
    }


    private val typePool = SparseArrayCompat<KClass<*>>()
    private val binderPool = SparseArrayCompat<TypedBinder<*>>()

    private var typeAutoIncrement = START

    @Synchronized
    fun <T : Any> register(klass: KClass<T>, binder: TypedBinder<T>) {
        val index = typePool.indexOfValue(klass)
        if (index > 0) {
            log { "$klass already register" }
            return
        }
        typePool.put(typeAutoIncrement, klass)
        binderPool.put(typeAutoIncrement, binder)
        typeAutoIncrement++
    }

    /**
     * unique key for this type
     */
    fun key(klass: KClass<*>): Int {
        val index = typePool.indexOfValue(klass)
        return typePool.keyAt(index)
    }

    fun getBinder(key: Int): TypedBinder<*> {
        return binderPool[key]
    }

}


/**
 * bind data to view
 *
 * implement [IProvider] 以支持远程实例化
 *
 */
abstract class TypedBinder<T : Any> : IProvider {

    override fun init(context: Context) {

    }

    private lateinit var adapter: TypedAdapter

    internal fun attachAdapter(adapter: TypedAdapter) {
        this.adapter = adapter
    }

    fun getAdapter(): TypedAdapter = adapter

    abstract fun onCreateViewHolder(parent: ViewGroup): ViewHolder

    abstract fun onBindViewHolder(holder: ViewHolder, item: T)

}


abstract class SimpleTypedBinder<T : Any> : TypedBinder<T>() {

    abstract val layoutId: Int

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(layoutId, parent)
    }
}

