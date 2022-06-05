package com.sl.web.server.controller

import com.sl.web.server.entity.event_log
import com.sl.web.server.response.wrapper
import com.sl.web.server.response.Wrapper
import kotlinx.coroutines.withContext
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

    class EventLogDto {
        var token = ""
        var type :Set<event_log.Type> = setOf()

        /**
         * 初步估计这个查询比较耗时
         * json { "start":0L, "end":1L }
         */
        var timeRange:LongRange? = null
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