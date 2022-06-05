package com.sl.web.server.service.impl

import com.sl.web.server.entity.event_log
import com.sl.web.server.mapper.LogMapper
import com.sl.web.server.mapper.UserMapper
import com.sl.web.server.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service

@Service
class LogServiceImpl : LogService {

    @Autowired
    lateinit var logMapper: LogMapper

    @Autowired
    lateinit var userMapper: UserMapper

    override suspend fun insert(log: event_log): Int {
        logMapper.saveAndFlush(log)
        return 1
    }

    override suspend fun queryByFilter(
        userId: String,
        types: Set<event_log.Type>,
        timeRange: LongRange?
    ): List<event_log> {
        val user = userMapper.findById(userId).let {
            if (it.isPresent) it.get() else return listOf()
        }
        if (types.isEmpty()) return listOf()
        val list = user.events.filter { it.type in types }.toList()
        return if (timeRange == null) list else list.filter { it.create_time in timeRange }
    }

    override suspend fun queryAll(userId: String): List<event_log> {
        val eventLog = event_log()
//        eventLog.user.userId = userId
//        eventLog.user.userId = userId
        val example = Example.of(eventLog, ExampleMatcher.matchingAny())
        return logMapper.findAll(example)
    }
}