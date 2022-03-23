package com.sl.web.server.code

import com.sl.web.server.utils.fromUnicode
import com.sl.web.server.utils.toUnicode

/**
 * 通过JNI 的方式提供对称式加密解密。
 * 顺便封装了 utf-8 unicode 中文在两者之间互转的方法
 *
 */
object Aes {

    const val publicKey = "RisklyBehaviorDetection"

    init {
        try {
            System.loadLibrary("libaes")
        }catch (e:UnsatisfiedLinkError){
            val path = this.javaClass.getResource("/native/").path
            System.load("${path}libaes.dll")
        }
    }

    private val unicodeRegex = Regex("""\\u[0-9a-fA-F]{4}""")

    /**
     * 中文-> unicode 编码
     */
    fun toUnicode(word: String): CharArray {
        fun charToUnicode(c: Char) = "\\u${Integer.toHexString(c.code)}".toCharArray().toTypedArray()
        val charList = ArrayList<Char>()
        word.toCharArray().forEach { c ->
            if ((c.code >= 0x4e00) && (c.code <= 0x9fbb)) {
                val temp = charToUnicode(c)
                charList.addAll(temp)
            } else {
                charList.add(c)
            }
        }
        return charList.toCharArray()
    }

    fun fromUnicode(array: CharArray): String {
        fun toChar(unicode: String) = unicode.toInt(16).toChar()
        val str = array.joinToString(separator = "", truncated = "")
        val result = str.replace(unicodeRegex) { it ->
            val temp = toChar(it.value.removePrefix("\\u"))
            "$temp"
        }
        return result
    }

    fun encrypt(word:String,key:String = publicKey):String{
        return encrypt(word.toUnicode(),key.toUnicode()).fromUnicode()
    }

    fun decrypt(word:String,key:String = publicKey):String{
        return decrypt(word.toCharArray(),key.toUnicode()).fromUnicode()
    }


    external fun encrypt(word: CharArray, key: CharArray): CharArray
    external fun decrypt(word: CharArray, key: CharArray): CharArray
    external fun version(): String

}