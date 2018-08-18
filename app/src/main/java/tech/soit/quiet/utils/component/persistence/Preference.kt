package tech.soit.quiet.utils.component.persistence

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import tech.soit.quiet.AppContext
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.createType
import kotlin.reflect.full.starProjectedType

/**
 * app's shared preferences
 */
@Suppress("UNCHECKED_CAST")
object Preference {


    /**
     * default application preference
     */
    private val default = PreferenceProperty<Any?>(PreferenceManager.getDefaultSharedPreferences(AppContext))

    /** player preference delegate */
    private val player = PreferenceProperty<Any?>(AppContext.getSharedPreferences("player", Context.MODE_PRIVATE))


    /**
     *@see PreferenceManager.getDefaultSharedPreferences
     */

    fun <T> default(): PreferenceProperty<T> {
        return default as PreferenceProperty<T>
    }

    /**
     * player preference ,save persistence config for Player
     *@param onChange String: the key which changed, T: the value changed
     */
    fun <T> player(onChange: ((key: String, value: T) -> Unit)? = null): PreferenceProperty<T> {
        if (onChange != null) {
            player.addListener(onChange as (String, Any?) -> Unit)
        }
        return player as PreferenceProperty<T>
    }

    /**
     * preference delegate,auto bind variable to preference
     *
     *     var volume by player(100)//the value will be auto changed if preference updated
     *     volume = 1 //will auto invoke [PreferenceProperty.setValue] method, and update preference
     *
     * TODO add object support
     */
    class PreferenceProperty<T>(
            private val preference: SharedPreferences
    ) : ReadWriteProperty<Any?, T?> {

        private fun onChange(key: String, value: T?) {
            listener.forEach {
                it.get()?.invoke(key, value)
            }
        }

        private val listener = ArrayList<WeakReference<(String, Any?) -> Unit>>()

        fun addListener(onChange: (String, Any?) -> Unit) {
            val ref = WeakReference(onChange)
            listener.removeAll { it.get() == null }
            listener.add(ref)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            val key = property.name
            val editor = preference.edit()
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
                is Boolean -> editor.putBoolean(key, value)
                null -> editor.remove(key)
                else -> error("")
            }
            onChange(key, value)
            editor.apply()
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            val key = property.name
            return when (property.returnType) {
                String::class.createType(nullable = true) -> preference.getString(key, null) as T?
                Int::class.starProjectedType -> preference.getInt(key, -1) as T
                String::class.starProjectedType -> preference.getFloat(key, -1f) as T
                String::class.starProjectedType -> preference.getLong(key, -1) as T
                Boolean::class.starProjectedType -> preference.getBoolean(key, false) as T
                else -> error("")
            }
        }
    }

}