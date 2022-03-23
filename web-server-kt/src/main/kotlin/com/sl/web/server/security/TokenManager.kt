package com.sl.web.server.security

import com.sl.web.server.code.Aes
import com.sl.web.server.service.UserService
import com.sl.web.server.utils.fromUnicode
import com.sl.web.server.utils.isLegal
import com.sl.web.server.utils.isLegalToken
import com.sl.web.server.utils.isTimeStamp
import java.util.*

/**
 * key 一律使用 publicKey
 * token 格式 userId - key + timestamp
 * timestamp [\\d]+
 */
object TokenManager {

    private const val tokenKey = "RisklyBehaviorDetection"

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
    fun validationToken(token: String,userService: UserService?):Boolean{
        if (!token.isLegalToken()) return false
        val origin = Aes.decrypt(token)
        origin.split("-${tokenKey}+")
            .takeIf { it.size == 2 && it[0].isLegal() && it[1].isTimeStamp() }
            .let { it ->
                it?:return false
                userService?.checkId(it[0])?.let { count->
                    // 说明账户不存在
                    if (count == 0) return false
                }
                val day = Date(it[1].toLong())
                val today = Date()
                // 判断token 是否过期
                return today.after(day)
            }
    }


    fun decodeToken(token: String): Pair<String, Long>? {
        // Aes 解码
        val origin = Aes.decrypt(token.toCharArray(), tokenKey.toCharArray()).fromUnicode()
        return origin.split("-${tokenKey}+")
            .takeIf { it.size == 2 && checkFormat(it[0], tokenKey, it[1]) }
            ?.let {
                it[0] to it[1].toLong()
            }
    }

    fun encodeToken(userId: String, timeStamp: Long): String? {
        if (!checkFormat(userId, tokenKey)) return null
        val origin = """${userId}-${tokenKey}+${timeStamp}"""
        return Aes.encrypt(origin.toCharArray(), tokenKey.toCharArray()).fromUnicode()
    }

    /**
     * 临时token 用于用户忘记密码通过邮箱链接重置密码
     */
    fun generateTokenForReset(userId: String){

    }

}