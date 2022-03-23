package com.sl.web.server.service

import com.sl.web.server.entity.Log
import com.sl.web.server.entity.Log.Type

/**
 * 后期加上过滤功能 todo
 */
interface LogService {

    suspend fun queryByType(type:Type): List<Log>

    suspend fun queryAll(): List<Log>

}