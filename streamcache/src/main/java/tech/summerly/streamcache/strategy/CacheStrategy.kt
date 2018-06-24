package tech.summerly.streamcache.strategy

import java.io.File

/**
 * author : YangBin
 */
interface CacheStrategy {

    /**
     * invoked when cache complete
     * @param file new cached file
     */
    fun onFileCached(file: File)

}