package com.sl.web.server.security

import com.sl.web.server.code.Aes
import com.sl.web.server.utils.*
import java.util.*

/**
 * key 一律使用 publicKey
 * token 格式 userId - key + timestamp
 * timestamp [\\d]+
 */
object TokenManager {

    const val tokenKey = "RisklyBehaviorDetection"

    fun checkFormat(userId: String, key: String, timeStamp: String? = null): Boolean {
        if (userId.isLegal() && key.isLegal()) {
            if (timeStamp == null)
                return true
            if (timeStamp.isTimeStamp())
                return true
        }
        return false
    }

    /**
     * 校验token是否有效
     */
    fun validationToken(token: String, checkId: (String)->Int):Boolean{
        if (!token.isLegalToken()) return false
        val origin = Aes.decrypt(token)
        origin.split("-${tokenKey}+")
            .takeIf { it.size == 2 && it[0].isLegal() && it[1].isTimeStamp() }
            .let { it ->
                it?:return false
                checkId.invoke(it[0]).let { count->
                    // 说明账户不存在
                    if (count == 0) return false
                }
                // 判断token 是否过期
                return it[1].toLong().isValid()
            }
    }


    fun decodeToken(token: String,key: String = tokenKey): Pair<String, Long>? {
        // Aes 解码
        val origin = Aes.decrypt(token.toCharArray(), key.toCharArray()).fromUnicode()
        return origin.split("-${key}+")
            .takeIf { it.size == 2 && checkFormat(it[0], key, it[1]) }
            ?.let {
                it[0] to it[1].toLong()
            }
    }

    fun encodeToken(userId: String, timeStamp: Long, key: String= tokenKey): String? {
        if (!checkFormat(userId, key)) return null
        val origin = """${userId}-${key}+${timeStamp}"""
        return Aes.encrypt(origin.toCharArray(), key.toCharArray()).fromUnicode()
    }

    /**
     * 临时token 用于用户忘记密码通过邮箱链接重置密码
     */
    fun generateTokenForReset(userId: String): String? {
        val key = tokenKey.uppercase() + "temp"
        val time = System.currentTimeMillis() + 1000 * 60 * 60 * 24 // 有效时间1天
        return encodeToken(userId, time, key)
    }

    fun validationResetToken(token: String,checkId: (String) -> Int):Boolean {
        if (!token.isLegalToken()) return false
        val origin = Aes.decrypt(token, tokenKey.uppercase()+"temp")
        origin.split("-${tokenKey.uppercase()+"temp"}+")
            .takeIf { it.size == 2 && it[0].isLegal() && it[1].isTimeStamp() }
            .let { it ->
                it?:return false
                checkId.invoke(it[0]).let { count->
                    // 说明账户不存在
                    if (count == 0) return false
                }
                // 判断token 是否过期
                return it[1].toLong().isValid()
            }
    }

    /**
     * 构造普通的token，用于访问系统其他功能
     */
    fun generateToken(userId: String):String?{
        val time = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 10 // 有效时间10天
        return encodeToken(userId,time)
    }

}