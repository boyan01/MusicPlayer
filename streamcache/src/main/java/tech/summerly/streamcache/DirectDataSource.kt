package tech.summerly.streamcache

import android.net.Uri
import tech.summerly.streamcache.source.FileSource
import tech.summerly.streamcache.source.HttpSource
import tech.summerly.streamcache.source.Source
import tech.summerly.streamcache.utils.emptyHeaderInjector

/**
 * Created by summer on 18-2-23
 *
 * 直接从Source获取数据,不提供缓存支持.
 */
class DirectDataSource(private val source: Source) : DataSource {

    companion object {

        operator fun invoke(uri: Uri): DirectDataSource {
            val source = if (uri.scheme == "file") {
                FileSource(uri.path)
            } else {
                HttpSource(uri.toString(), emptyHeaderInjector)
            }
            return DirectDataSource(source)
        }

    }


    override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
        return source.read(buffer, position, offset, size)
    }

    override fun getSize(): Long {
        return source.size
    }

    override fun close() {
        source.close()
    }
}