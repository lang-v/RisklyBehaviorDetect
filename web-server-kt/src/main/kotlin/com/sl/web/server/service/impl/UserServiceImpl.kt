package com.sl.web.server.service.impl

import com.sl.web.server.code.Aes
import com.sl.web.server.entity.User
import com.sl.web.server.mapper.UserMapper
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.UserService
import com.sl.web.server.utils.fromUnicode
import com.sl.web.server.utils.toUnicode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userMapper: UserMapper

    override suspend fun login(userId: String, password: String, timestamp: Long): String {
        val temp = Aes.decrypt(password.toCharArray(), timestamp.toString().toCharArray()).fromUnicode()
        val realPassword = Aes.encrypt(temp.toUnicode(),Aes.publicKey.toCharArray()).fromUnicode()
        val user = User().apply {
            this.userId = userId
            this.password = realPassword
        }
        val example = Example.of(user, ExampleMatcher.matchingAny().withIgnoreNullValues())

        userMapper.findOne(example).takeIf { it.isPresent }?.get().let {
            if (it == null) {
                return ""
            }else{
                // todo 这里需要修改，可以考虑改为传入function，而不是service
                if (!TokenManager.validationToken(it.token,userService = this)){

                }
//                todo 明天写
                return ""
            }
        }
    }

    override suspend fun register(user: User): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateUsername(token: String, newUsername: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun update(userId: String, password: String, newPassword: String): Int {
        TODO("Not yet implemented")
    }

    override suspend fun reset(userId: String, newPassword: String): User {
        TODO("Not yet implemented")
    }

    override fun checkId(userId: String): Int {
        userMapper.findByIdOrNull(userId) ?: return 0
        return 1
    }

    override fun checkEmail(email: String): Int {
        val user = User()
        user.email = email
        val example = Example.of(user, ExampleMatcher.matchingAny().withIgnoreNullValues())
        return if (userMapper.findOne(example).isPresent) 1 else 0
    }


}