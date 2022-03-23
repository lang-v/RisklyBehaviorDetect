package com.sl.web.server.utils

import com.sl.web.server.code.Aes

fun String.toUnicode():CharArray{
    return Aes.toUnicode(this)
}

fun CharArray.fromUnicode():String{
    return Aes.fromUnicode(this)
}

val timeStampRegex = Regex("""[\d]+""")
fun String.isTimeStamp():Boolean{
    return timeStampRegex.matches(this)
}

val illegalCharRegex = Regex("""[\u0000?#./\\%^&*\-_=+`~'"{}\[\]()（）— @$·‘“《》<>;；。|]""")

/**
 * 简单检测下字符是否合规
 * @return true 合规
 */
fun String.isLegal():Boolean{
    val origin = this.trim()
    return !origin.contains(illegalCharRegex)
}

val illegalTokenRegex = Regex("""[^0-9a-zA-Z=]""")

/**
 * 校验字符串是否是合规token
 */
fun String.isLegalToken():Boolean{
    return this.matches(illegalTokenRegex)
}