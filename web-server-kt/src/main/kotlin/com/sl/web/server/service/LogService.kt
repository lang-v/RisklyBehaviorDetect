package com.sl.web.server.service

import com.sl.web.server.entity.EventLog
import com.sl.web.server.entity.EventLog.Type

/**
 * 后期加上过滤功能 todo
 */
interface LogService {

    @Deprecated("@see also UserService")
    suspend fun insert(log: EventLog):Int

    suspend fun queryByFilter(userId:String, types:Set<Type>,timeRange: LongRange?): List<EventLog>

    @Deprecated("查询功能直接由UserService替代")
    suspend fun queryAll(userId: String): List<EventLog>

}