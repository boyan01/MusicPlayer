package tech.summerly.quiet.commonlib.utils.download

import android.os.Environment
import okhttp3.*
import tech.summerly.quiet.commonlib.LibModule
import java.io.File
import java.io.IOException

class DownloadTask(
        private val request: DownloadRequest
) {

    companion object {
        private val sClient = OkHttpClient.Builder()
                .build()

        private val DOWNLOAD_PATH = LibModule.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    }

    val id = request.buildId()

    private fun createFile(url: String): File {
        val music = request.music
        val suffix = url.substringAfterLast('.', "mp3")
        val name = "%s - %s.$suffix".format(music.title, music.artist)
        return File(name)
    }


    internal fun startDownload() {
        //todo add map table

        val url = request.music.getUrl(request.bitrate)

        val fileName = "%s - %s".format(request.music.title, request.music.artist.joinToString { "/" })

        val request = Request.Builder().url(url).build()
        sClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }
        })
    }


}