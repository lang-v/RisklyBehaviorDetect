package com.sl.web.server.service

import com.sl.web.server.entity.EventLog
import com.sl.web.server.entity.EventLog.Type

/**
 * 后期加上过滤功能 todo
 */
interface LogService {

    suspend fun insert(log: EventLog):Int

    suspend fun queryByType(type:Type): List<EventLog>

    suspend fun queryAll(): List<EventLog>

}