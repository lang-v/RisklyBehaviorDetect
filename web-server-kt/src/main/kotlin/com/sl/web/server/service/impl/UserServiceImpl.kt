package com.sl.web.server.service.impl

import com.sl.web.server.code.Aes
import com.sl.web.server.entity.user_info
import com.sl.web.server.mapper.UserMapper
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userMapper: UserMapper

    override suspend fun query(userId: Set<String>): List<user_info> {
        return userMapper.findAllById(userId)
    }

    override suspend fun queryAll(): List<user_info> {
        return userMapper.findAll()
    }

    override suspend fun login(userId: String, password: String, timestamp: Long): user_info? {
        val user = userMapper.findById(userId).let {
            if (it.isPresent) it.get() else return null
        }
        val realPassword = Aes.encrypt(password)
        if (realPassword != user.password)
            return null
        if (!TokenManager.validationToken(user.token,this::checkId)){
            val newToken = TokenManager.generateToken(userId)?:return null
            user.token = newToken
        }
        return user
    }

    override suspend fun register(user: user_info): Int {
        user.token = TokenManager.generateToken(user.user_id)!!
        userMapper.saveAndFlush(user)
        return 1
    }

    override suspend fun updateUsername(userId: String, newUsername: String): Int {
        val user = userMapper.findById(userId).let {
            if (it.isPresent) it.get()
            else return 0
        }
        user.username = newUsername
        userMapper.saveAndFlush(user)
        return 1
    }

    override suspend fun update(userId: String, newPassword: String): Int {
        val user = userMapper.findById(userId).get()
//        字段加密后写入数据库，登录时直接用密文进行对比
        val code = Aes.encrypt(newPassword)
        user.password = code
        userMapper.saveAndFlush(user)
        return 1
    }

    override suspend fun update(user: user_info): Boolean {
        userMapper.saveAndFlush(user)
        return true
    }

    override suspend fun update(userId: String, password: String, newPassword: String): Int {
        val user = userMapper.findById(userId).get()
        val realPassword = Aes.encrypt(password)
        if (realPassword != user.password) {
            return 0
        }
//        字段加密后写入数据库，登录时直接用密文进行对比
        val code = Aes.encrypt(newPassword)
        user.password = code
        userMapper.saveAndFlush(user)
        return 1
    }


    override fun checkId(userId: String): Int {
        userMapper.findByIdOrNull(userId) ?: return 0
        return 1
    }

    override suspend fun checkEmail(email: String): Int {
        val user = user_info()
        user.email = email
        val example = Example.of(user, ExampleMatcher.matchingAny().withIgnoreNullValues())
        return if (userMapper.findOne(example).isPresent) 1 else 0
    }

    override suspend fun checkIdAndEmail(userId: String, email: String):Boolean {
        val user = user_info()
        user.user_id = userId
        user.email = email
        val example = Example.of(user, ExampleMatcher.matchingAny().withIgnoreNullValues())
        return userMapper.findOne(example).isPresent
    }


}