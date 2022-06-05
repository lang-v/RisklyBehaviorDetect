package com.sl.web.server.service

import com.sl.web.server.entity.user_info


interface UserService {

    suspend fun query(userId: Set<String>):List<user_info>

    suspend fun queryAll():List<user_info>

    suspend fun login(userId: String, password: String, timestamp: Long): user_info?

    suspend fun register(user: user_info): Int

    suspend fun updateUsername(userId: String, newUsername: String): Int

    suspend fun update(userId: String, newPassword: String): Int

    suspend fun update(user: user_info):Boolean

    suspend fun update(userId: String, password:String, newPassword: String): Int

    fun checkId(userId: String): Int

    suspend fun checkEmail(email: String): Int

    suspend fun checkIdAndEmail(userId: String,email: String): Boolean

}