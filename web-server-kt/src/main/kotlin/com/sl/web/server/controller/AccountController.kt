package com.sl.web.server.controller

import com.sl.web.server.code.Aes
import com.sl.web.server.dto.AccountDto
import com.sl.web.server.email.EmailSender
import com.sl.web.server.email.generateResetLinkEmail
import com.sl.web.server.email.generateVerificationCodeEmail
import com.sl.web.server.entity.EventLog
import com.sl.web.server.entity.User
import com.sl.web.server.entity.ValidationCode
import com.sl.web.server.response.SimpleToken
import com.sl.web.server.response.Wrapper
import com.sl.web.server.response.wrapper
import com.sl.web.server.response.wrapperWithToken
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.LogService
import com.sl.web.server.service.UserService
import com.sl.web.server.service.ValidationCodeService
import com.sl.web.server.utils.EventLogBuilder
import com.sl.web.server.utils.isValid
import com.sl.web.server.utils.toDateTime
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 原来打算将验证码单独踢出去，后面发现验证码和账户耦合太重拆不开
 */

@RestController
@RequestMapping("/account")
class AccountController : BasicController() {

    @Autowired
    lateinit var validationCodeService: ValidationCodeService
    @GetMapping("/apply_code")
    suspend fun applyCode(
        @RequestParam(name = "user_id", required = true) user_id: String,
        @RequestParam(name = "email", required = true) email: String,
        @RequestParam(name = "operation", required = true) operation: String,
        @RequestParam(name = "reset_url", required = false) resetUrl: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            var bean = validationCodeService.query(user_id)
            if (bean == null) {
                bean = ValidationCode()
                bean.userId = user_id
                bean.email = email
                bean.lastApplyTime = Date(10000L).time
            }

            val time = bean.lastApplyTime
            // 限制每个邮箱 60s 发一次验证码
            if ((System.currentTimeMillis() - time) < 60 * 1000) return@withContext "".wrapper(101, "请求太频繁，请稍后再试！")
            when (operation) {
                "reset" -> {
                    // 重置密码
                    if (!userService.checkIdAndEmail(user_id, email)) {
                        return@withContext "".wrapper(404, "该账户或邮箱并未注册")
                    }

                    val resetToken = TokenManager.generateTokenForReset(user_id)
                        ?: return@withContext "".wrapper(201, "验证失败")
                    val user = userService.query(setOf(user_id)).let {
                        if (it.size == 1) {
                            it[0].token = resetToken
                        }
                        it[0]
                    }
                    if (!userService.update(user)) {
                        return@withContext "".wrapper(201, "请求失败")
                    }
                    validationCodeService.saveOrUpdate(bean)
                    EmailSender.Builder()
                        .init()
                        .setTitle("重置密码")
                        .setContent(generateResetLinkEmail("$resetUrl?token=$resetToken"))
                        .setReceiver(arrayOf(email))
                        .build()
                        .send()
                    return@withContext "".wrapper(200, "邮件发送成功，请检查邮箱。")
                }
                "register" -> {
                    if (userService.checkIdAndEmail(user_id, email)) {
                        return@withContext "".wrapper(203, "该账户或邮箱已注册")
                    }
                    // 注册
                    val code = validationCodeService.generateCode()
                    bean.userId = user_id
                    bean.code = code
                    // 五分钟内有效
                    bean.lastApplyTime = System.currentTimeMillis() + 60 * 5 * 1000
                    validationCodeService.saveOrUpdate(bean)
                    EmailSender.Builder()
                        .init()
                        .setTitle("注册账户")
                        .setContent(generateVerificationCodeEmail("注册账户", code))
                        .setReceiver(arrayOf(email))
                        .build()
                        .send()
//                    val log = EventLog().apply {
//                        createTime = Date().toString()
//                        type = EventLog.Type.Register
//                        userId = user_id
//                        content =
//                    }
                    return@withContext "".wrapper(200, "验证码发送成功。")
                }
            }
            "".wrapper(100, "未知错误")
        }
    }

    @PostMapping("/query_all")
    suspend fun queryAll(
        @RequestBody(required = true) accountDto: AccountDto
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(accountDto.token, userService::checkId)) {
                return@withContext "".wrapper(201, "验证失败")
            }
            val user_id =
                TokenManager.decodeToken(accountDto.token)?.first ?: return@withContext "".wrapper(202, "验证失败")
            val userList =
                userService.queryAll().filter { it.userId != user_id }.map { it.userId to it.username }.toList()
            return@withContext userList.wrapper(200, "查询成功")
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
        @RequestBody(required = true) dto: AccountDto
    ): Wrapper<User> {
        return withContext(coroutineContext) {
//            val bean = validationCodeService.query(dto.user_id) ?: return@withContext "".wrapperWithToken(100, "验证码有误")
//            if (bean.code !=dto.validation_code) return@withContext "".wrapperWithToken(101, "验证码错误")
//            else
//                if (!bean.lastApplyTime.isValid())
//                    return@withContext "".wrapperWithToken(102, "验证码失效")
            val user = userService.login(dto.user_id, dto.password, dto.timestamp)
                ?: return@withContext User().wrapper(201, "登录失败")
//            val user = userService.query(setOf(userId))[0]
            EventLogBuilder()
                .setCreateTime(System.currentTimeMillis())
                .setType(EventLog.Type.Login)
                .setContent("user ${user.username} : login")
                .saveTo(user)
            userService.update(user)
            user.wrapper(200, "登录成功")
        }
    }

    @PostMapping("/register")
    suspend fun register(
        @RequestBody(required = true) dto: AccountDto
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            val code = validationCodeService.query(dto.user_id) ?: return@withContext "".wrapper(100, "验证码有误")
            if (code.code != dto.validation_code)
                return@withContext "".wrapper(101, "验证码错误")
            else
                if (!code.lastApplyTime.isValid())
                    return@withContext "".wrapper(102, "验证码失效")

            val user = User().apply {
                this.userId = dto.user_id
                this.email = dto.email
                this.username = dto.username
                this.password = Aes.encrypt(dto.password)
            }
            val count = userService.register(user)
            if (count != 1) {
                return@withContext "".wrapper(201, "注册失败")
            }
            val newUser = userService.query(setOf(dto.user_id))[0]
            EventLogBuilder()
                .setCreateTime(System.currentTimeMillis())
                .setType(EventLog.Type.Register)
                .setContent("user ${newUser.username} : register")
                .saveTo(newUser)
            userService.update(newUser)
            "".wrapper(200, "注册成功")
        }
    }

    @PostMapping("/update")
    suspend fun update(
        @RequestBody(required = true) dto: AccountDto
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            TokenManager.decodeToken(dto.token)?.first ?: return@withContext "".wrapper(101, "验证失败")
            if (!TokenManager.validationToken(dto.token, userService::checkId)) {
                return@withContext "".wrapper(101, "验证失败")
            }
            val count = userService.update(dto.user_id, dto.password, dto.new_password)
            if (count == 0) {
                return@withContext "".wrapper(201, "信息有误")
            }
            val newUser = userService.query(setOf(dto.user_id))[0]
            EventLogBuilder()
                .setCreateTime(System.currentTimeMillis())
                .setType(EventLog.Type.Update)
                .setContent("user ${newUser.username} : update password [\"${dto.password}\" -> \"${dto.new_password}\"]")
                .saveTo(newUser)
            userService.update(newUser)
            "".wrapper(200, "修改成功")
        }
    }

    @PostMapping("/update_username")
    suspend fun updateUsername(
        @RequestBody(required = true) dto: AccountDto
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(dto.token, userService::checkId)) {
                return@withContext "".wrapper(301, "token过期")
            }
            val user_id =
                TokenManager.decodeToken(token = dto.token)?.first ?: return@withContext "".wrapper(301, "链接失效或者其他错误")

            val count = userService.updateUsername(user_id, dto.new_username)
            if (count == 0) {
                return@withContext "".wrapper(201, "修改失败")
            }
            val newUser = userService.query(setOf(dto.user_id))[0]
            EventLogBuilder()
                .setCreateTime(System.currentTimeMillis())
                .setType(EventLog.Type.Update)
                .setContent("user ${newUser.username} : update username [\"${dto.username}\" -> \"${dto.new_username}\"]")
                .saveTo(newUser)
            userService.update(newUser)
            "".wrapper(200, "修改成功")
        }
    }

    @PostMapping("/reset")
    suspend fun reset(
        @RequestBody(required = true) dto: AccountDto
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationResetToken(dto.validation_token, userService::checkId)) {
                return@withContext "".wrapper(301, "链接失效或者其他错误")
            }
            val info = TokenManager.decodeToken(dto.validation_token, TokenManager.tokenKey.uppercase() + "temp")
                ?: return@withContext "".wrapper(300, "信息有误")
            val count = userService.update(info.first, dto.new_password)
            if (count != 1) {
                return@withContext "".wrapper(302, "修改失败")
            }
            // fixed 这里是重置密码，dto 中没有userId
            val newUser = userService.query(setOf(info.first))[0]
            EventLogBuilder()
                .setCreateTime(System.currentTimeMillis())
                .setType(EventLog.Type.Reset)
                .setContent("user ${newUser.username} : reset ")
                .saveTo(newUser)
            userService.update(newUser)
            "".wrapper(200, "修改成功")
        }
    }


}