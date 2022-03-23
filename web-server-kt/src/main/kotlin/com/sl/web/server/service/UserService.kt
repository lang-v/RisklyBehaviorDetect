package com.sl.web.server.service

import com.sl.web.server.entity.User


interface UserService {

    suspend fun login(userId: String, password: String, timestamp: Long): String

    suspend fun register(user: User): Int

    suspend fun updateUsername(token: String, newUsername: String): User

    suspend fun update(userId: String, password: String, newPassword: String): Int

    //    慎用，无须原来密码直接修改现有密码。需配合邮件重置链接使用
    suspend fun reset(userId: String, newPassword: String): User

    fun checkId(userId: String): Int

    fun checkEmail(email: String): Int

}