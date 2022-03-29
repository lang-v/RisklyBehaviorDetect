package com.sl.web.server.controller

import com.sl.web.server.code.Aes
import com.sl.web.server.email.EmailSender
import com.sl.web.server.email.generateResetLinkEmail
import com.sl.web.server.email.generateVerificationCodeEmail
import com.sl.web.server.entity.User
import com.sl.web.server.entity.ValidationCode
import com.sl.web.server.response.SimpleToken
import com.sl.web.server.response.Wrapper
import com.sl.web.server.response.wrapper
import com.sl.web.server.response.wrapperWithToken
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.UserService
import com.sl.web.server.service.ValidationCodeService
import com.sl.web.server.utils.isEmail
import com.sl.web.server.utils.isValid
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * 原来打算将验证码单独踢出去，后面发现验证码和账户耦合太重拆不开
 */

@RestController
@RequestMapping("/account")
class AccountController : BasicController() {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var validationCodeService: ValidationCodeService

    @GetMapping("/apply_code")
    suspend fun applyCode(
        @RequestParam(name = "user_id", required = true) user_id: String,
        @RequestParam(name = "email", required = true) email: String,
        @RequestParam(name = "operation", required = true) operation: String,
        @RequestParam(name = "reset_url") resetUrl: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            var bean = validationCodeService.query(user_id)
            if (bean == null) {
                bean = ValidationCode()
                bean.userId = user_id
                bean.email = email
                bean.lastApplyTime = Date(0L).toString()
            }

            val time = Date(bean.lastApplyTime).time
            // 限制每个邮箱 60s 发一次验证码
            if ((System.currentTimeMillis() - time) < 60) return@withContext "".wrapper(101, "请求太频繁，请稍后再试！")
            when (operation) {
                "reset" -> {
                    // 重置密码
                    if (!userService.checkIdAndEmail(user_id, email)) {
                        return@withContext "".wrapper(404, "该账户或邮箱并未注册")
                    }

                    TokenManager.generateTokenForReset(user_id)
                    EmailSender.Builder()
                        .init()
                        .setTitle("重置密码")
                        .setContent(generateResetLinkEmail(resetUrl))
                        .setReceiver(email)
                        .send()
                    validationCodeService.saveOrUpdate(bean)
                    return@withContext "".wrapper(200, "邮件发送成功，请检查邮箱。")
                }
                "register" -> {
                    // 注册
                    val code = validationCodeService.generateCode()
                    EmailSender.Builder()
                        .init()
                        .setTitle("注册账户")
                        .setContent(generateVerificationCodeEmail("注册账户", code))
                        .setReceiver(email)
                        .send()
                    bean.code = code
                    validationCodeService.saveOrUpdate(bean)
                    return@withContext "".wrapper(200, "验证码发送成功。")
                }
            }
            "".wrapper(100, "未知错误")
        }
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
        @RequestBody(required = true) timestamp: Long,
        @RequestBody(required = true) validation_code: String
    ): Wrapper<SimpleToken> {
        return withContext(coroutineContext) {
            val bean = validationCodeService.query(user_id) ?: return@withContext "".wrapperWithToken(100, "验证码有误")
            if (bean.code != validation_code) return@withContext "".wrapperWithToken(101, "验证码错误")
            else
                if (!bean.lastApplyTime.isValid())
                    return@withContext "".wrapperWithToken(102, "验证码失效")
            val token = userService.login(user_id, password, timestamp)
            if (token.isEmpty()) {
                return@withContext "".wrapperWithToken(201, "登录失败")
            }
            token.wrapperWithToken(200, "注册成功")
        }
    }

    @PostMapping("/register")
    suspend fun register(
        @RequestBody(required = true) user_id: String,
        @RequestBody(required = true) username: String,
        @RequestBody(required = true) email: String,
        @RequestBody(required = true) password: String,
        @RequestBody(required = true) validation_code: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            val code = validationCodeService.query(user_id) ?: return@withContext "".wrapper(100, "验证码有误")
            if (code.code != validation_code)
                return@withContext "".wrapper(101, "验证码错误")
            else
                if (!code.lastApplyTime.isValid())
                    return@withContext "".wrapper(102, "验证码失效")

            val user = User().apply {
                this.userId = user_id
                this.email = email
                this.username = username
                this.password = Aes.encrypt(password)
            }
            val count = userService.register(user)
            if (count != 1) {
                return@withContext "".wrapper(201, "注册失败")
            }
            "".wrapper(200, "注册成功")
        }
    }

    @PostMapping("/update")
    suspend fun update(
        @RequestBody(required = true) userId: String,
        @RequestBody(required = true) password: String,
        @RequestBody(required = true) new_password: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            val count = userService.update(userId, password, new_password)
            if (count == 0) {
                return@withContext "".wrapper(201, "信息有误")
            }
            "".wrapper(200, "修改成功")
        }
    }

    @PostMapping("/update_username")
    suspend fun updateUsername(
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) new_username: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            val count = userService.updateUsername(token, new_username)
            if (count == 0) {
                return@withContext "".wrapper(201, "修改失败")
            }
            "".wrapper(200, "修改成功")
        }
    }

    @PostMapping("/reset")
    suspend fun reset(
        @RequestBody(required = true) validation_token: String,
        @RequestBody(required = true) new_password: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(validation_token, userService::checkId)) {
                return@withContext "".wrapper(301, "链接失效或者其他错误")
            }
            val info = TokenManager.decodeToken(validation_token) ?: return@withContext "".wrapper(300, "信息有误")
            val count = userService.update(info.first, new_password)
            if (count != 1) {
                return@withContext "".wrapper(300, "信息有误")
            }
            "".wrapper(200, "修改成功")
        }
    }


}