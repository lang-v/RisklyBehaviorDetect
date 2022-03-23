package com.sl.web.server.controller

import com.sl.web.server.response.Wrapper
import com.sl.web.server.response.wrapper
import com.sl.web.server.service.UserService
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.coroutines.CoroutineContext

@RestController
@RequestMapping("/account")
class AccountController : BasicController() {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/apply_code")
    suspend fun applyCode(
        @RequestParam(name = "operation", required = true) operation: String,
        @RequestParam(name = "user_id", required = true) user_id: String,
    ):Wrapper<String> {
        when(operation){
            "reset" -> {

            }
            "register" ->{

            }
            else->{
                return "".wrapper(201,"未知请求")
            }
        }
        return "".wrapper(0,"")
    }

    @GetMapping("/check")
    suspend fun checkId(@RequestParam(name = "user_id") user_id: String): Wrapper<Any> {
        val result = withContext(coroutineContext) {
            userService.checkId(user_id)
        }
        return result.wrapper(
            200, when (result) {
                1 -> "账户已存在"
                0 -> "账户可用"
                else -> "未知错误"
            }
        )
    }

    @RequestMapping("/check_email")
    suspend fun checkEmail(@RequestParam("email") email: String): Wrapper<Int> {
        val result = withContext(coroutineContext) {
            userService.checkEmail(email)
        }
        return result.wrapper(
            200, when (result) {
                1 -> "邮箱已注册"
                0 -> "邮箱可用"
                else -> "未知错误"
            }
        )

    }

    @PostMapping("/login")
    suspend fun login(
        @RequestBody(required = true) user_id: String,
        @RequestBody(required = true) password: String,
        @RequestBody(required = true) timestamp: Long
    ) {
        userService.login(user_id, password, timestamp)

    }

    @PostMapping("/register")
    suspend fun register(
        @RequestBody(required = true) user_id: String,
        @RequestBody(required = true) username: String,
        @RequestBody(required = true) password: String,
        @RequestBody(required = true) validation_code: String
    ) {

    }

    @PostMapping("/update")
    suspend fun update(
        @RequestBody(required = true) userId: String,
        @RequestBody(required = true) password: String,
        @RequestBody(required = true) new_password: String
    ) {

    }

    @PostMapping("/update_username")
    suspend fun updateUsername(
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) new_username: String
    ) {

    }

    @PostMapping("/reset")
    suspend fun reset(
        @RequestBody(required = true) validation_token: String,
        @RequestBody(required = true) new_password: String
    ) {

    }


}