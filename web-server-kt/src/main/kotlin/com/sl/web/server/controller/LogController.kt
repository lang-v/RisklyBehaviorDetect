package com.sl.web.server.controller

import com.sl.web.server.dto.EventLogDto
import com.sl.web.server.response.wrapper
import com.sl.web.server.entity.EventLog.Type
import com.sl.web.server.response.Wrapper
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.LogService
import com.sl.web.server.service.UserService
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/log")
class LogController : BasicController() {

    @PostMapping("/all")
    suspend fun all(
        @RequestBody(required = true) dto: EventLogDto
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            val userId = checkTokenAndBackID(dto.token) {
                return@withContext "".wrapper(201, "token失效")
            }
            val list = userService.query(setOf(userId))
            if (list.size != 1) {
                return@withContext "".wrapper(202, "查询失败")
            }
            val user = list[0]
            return@withContext user.events.wrapper(200, "查询成功")
//            return@withContext list.wrapper(200, "查询成功")
        }
    }

    @PostMapping("/filter")
    suspend fun queryByFilter(
        @RequestBody(required = true) dto: EventLogDto
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            val userId = checkTokenAndBackID(dto.token) {
                return@withContext "".wrapper(201, "token 失效")
            }
            val list = logService.queryByFilter(userId, dto.type, dto.timeRange)
            list.wrapper(200, "查询成功")
        }
    }
}