package com.sl.web.server.controller

import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.LogService
import com.sl.web.server.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.springframework.beans.factory.annotation.Autowired
import kotlin.coroutines.CoroutineContext

open class BasicController:CoroutineScope {

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var logService: LogService

    inline fun checkTokenAndBackID(token: String, error:()->Unit):String {
        if (!TokenManager.validationToken(token,userService::checkId))
            error.invoke()
        return TokenManager.decodeToken(token)!!.first
    }
}