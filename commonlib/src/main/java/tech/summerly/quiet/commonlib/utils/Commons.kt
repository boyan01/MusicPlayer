package tech.summerly.quiet.commonlib.utils

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * format milli to MM:ss , ex : 12:00
 */
fun Number.toMusicTimeStamp(): String = with(toLong() / 1000) {
    val second = this % 60
    val minute = this / 60
    String.format("%02d:%02d", minute, second)
}


@Route(path = "/service/json")
class JsonServiceImpl : SerializationService {

    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        error("未接入!")
    }

    override fun init(context: Context?) {

    }

    override fun object2Json(instance: Any?): String? {
        return Gson().toJson(instance)
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type): T? {
        return Gson().fromJson(input, clazz)
    }

}