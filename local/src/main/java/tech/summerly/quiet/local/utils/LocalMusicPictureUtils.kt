package tech.summerly.quiet.local.utils

import org.jetbrains.anko.attempt
import tech.summerly.quiet.local.LocalModule
import java.io.File

/**
 * Created by summer on 17-12-24
 */

object LocalMusicPictureUtils {


    private const val ARTWORK_FOLDER_NAME = "artwork"

    private val artworkCache: File by lazy {
        File(LocalModule.externalCacheDir, ARTWORK_FOLDER_NAME).apply { mkdirs() }
    }

    /**
     * save picture to [artworkCache]
     */
    fun saveEmbeddedPicture(fileName: String,
                            data: ByteArray,
                            replaceIfExist: Boolean,
                            dir: File = artworkCache): String? {
        return attempt {
            val file = File(dir, fileName)
            if (file.exists() && !replaceIfExist) {
                return@attempt file.path
            }
            if (file.exists()) {
                file.delete()
            }
            file.parentFile.mkdirs()
            file.createNewFile()
            file.outputStream().use {
                it.write(data)
                it.flush()
            }
            return@attempt file.path
        }.value
    }

}
