package tech.soit.quiet.repository.netease

import android.util.Base64
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/22
 * desc   : 改编自 https://github.com/Binaryify/NeteaseCloudMusicApi/blob/master/util/crypto.js
 */
object Crypto {
    private const val keys = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    private const val modulus =
            "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76" +
                    "d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7" +
                    "a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece046" +
                    "2db0a22b8e7"
    private const val nonce = "0CoJUm6Qyw8W8jud"
    private const val publicKey = "010001"


    /**
     * 对云音乐 api 的请求进行加密
     * 返回的map中 params 是加密的请求数据
     *  encSecKey 是请求数据的密钥
     */
    fun encrypt(obj: Any): Map<String, String> {
        val text = Gson().toJson(obj)
        return encrypt(text)
    }

    fun encrypt(json: String): Map<String, String> {
        println("加密请求 :$json")
        val secKey = createSecretKey()
        //对参数请求进行以 [nonce] 加密后的结果再次使用 createSecretKry() 产生的随机数进行加密
        val encText = aesEncrypt(aesEncrypt(json, nonce), secKey)
        //对第二次使用的随机串进行加密,以便在服务器端解析出具体的参数请求
        val encSecKey = rsaEncrypt(secKey)
        return mapOf("params" to encText, "encSecKey" to encSecKey)
    }


    /**
     * 随机一个 16 位的 字母数字序列
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun createSecretKey(size: Int = 16): String {
        val key = StringBuilder()
        for (i in 1..size) {
            val pos = Math.floor(Math.random() * keys.length).toInt()
            key.append(keys[pos])
        }
        return key.toString()
    }

    /**
     * @param text 带加密字符串
     * @param secKey 加密的密码
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun aesEncrypt(text: String, secKey: String): String {
        val iv = "0102030405060708".toByteArray()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(secKey.toByteArray(), "AES"), IvParameterSpec(iv))
        val results = cipher.doFinal(text.toByteArray())
        return android.util.Base64.encodeToString(results, Base64.DEFAULT)
    }

    /**
     * 填充至 256 字符长度
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun zFill(str: String, size: Int = 256): String {
        if (str.length >= size) {
            return str
        }
        val builder = StringBuilder(str)
        while (builder.length < size) builder.insert(0, 0)
        return builder.toString()
    }

    private fun rsaEncrypt(text: String): String {
        val biText = BigInteger(text.reversed().toByteArray())
        val biEx = BigInteger(publicKey, 16)
        val biMod = BigInteger(modulus, 16)
        val biRet = biText.modPow(biEx, biMod)
        return zFill(biRet.toString(16))
    }


    fun String.md5(): String {

        val md = MessageDigest.getInstance("MD5")
        val array = md.digest(toByteArray())
        val sb = StringBuffer()
        for (i in array.indices) {
            sb.append(Integer.toHexString(array[i].toInt() and 0xFF or 0x100).substring(1, 3))
        }
        return sb.toString()

    }


}