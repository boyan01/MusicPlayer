package tech.summerly.quiet.local.database.converter

import android.arch.persistence.room.TypeConverter
import tech.summerly.quiet.commonlib.bean.MusicType

/**
 * author : yangbin10
 * date   : 2017/12/12
 */
class ArchTypeConverter {
    @TypeConverter
    fun convertToString(type: MusicType): String = type.name

    @TypeConverter
    fun convertFromString(name: String): MusicType = MusicType.valueOf(name)
}