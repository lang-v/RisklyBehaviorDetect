package com.sl.web.server.utils

import com.sl.web.server.code.Aes
import java.util.*

fun String.toUnicode(): CharArray {
    return Aes.toUnicode(this)
}

fun CharArray.fromUnicode(): String {
    return Aes.fromUnicode(this)
}

val timeStampRegex = Regex("""[\d]+""")
fun String.isTimeStamp(): Boolean {
    return timeStampRegex.matches(this)
}

val illegalCharRegex = Regex("""[\u0000?#./\\%^&*\-_=+`~'"{}\[\]()（）— @$·‘“《》<>;；。|]""")

/**
 * 简单检测下字符是否合规
 * @return true 合规
 */
fun String.isLegal(): Boolean {
    val origin = this.trim()
    return !origin.contains(illegalCharRegex)
}

val illegalTokenRegex = Regex("""[^0-9a-zA-Z=]""")

/**
 * 校验字符串是否是合规token
 */
fun String.isLegalToken(): Boolean {
    return this.matches(illegalTokenRegex)
}

val legalEmail = Regex("""^[0-9a-zA-Z][0-9a-zA-Z.]+@[0-9a-zA-Z]+\.[a-zA-Z]+$""")
fun String.isEmail() = this.matches(legalEmail)

/**
 * 检测时间戳是否有效
 */
fun String.isValid(): Boolean {
    val isTimeStamp = this.isTimeStamp()
    if (!isTimeStamp)
        return false
    val time = this.toLong()
    val thatDay = Date(time)
    val today = Date()

    return !today.after(thatDay)
}