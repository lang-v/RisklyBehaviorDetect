package com.sl.web.server.service

import com.sl.web.server.entity.User


interface UserService {

    suspend fun query(userId: Set<String>):List<User>

    suspend fun login(userId: String, password: String, timestamp: Long): User?

    suspend fun register(user: User): Int

    suspend fun updateUsername(userId: String, newUsername: String): Int

    suspend fun update(userId: String, newPassword: String): Int

    suspend fun update(user: User):Boolean

    suspend fun update(userId: String, password:String, newPassword: String): Int

    fun checkId(userId: String): Int

    suspend fun checkEmail(email: String): Int

    suspend fun checkIdAndEmail(userId: String,email: String): Boolean

}