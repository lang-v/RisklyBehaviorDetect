package com.sl.web.server.service.impl

import com.sl.web.server.entity.EventLog
import com.sl.web.server.mapper.LogMapper
import com.sl.web.server.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service

@Service
class LogServiceImpl : LogService {

    @Autowired
    lateinit var logMapper: LogMapper

    override suspend fun queryByType(type: EventLog.Type): List<EventLog> {
        val eventLog = EventLog()
        eventLog.type = type
        val example = Example.of(eventLog, ExampleMatcher.matchingAny().withIgnoreNullValues())
        return logMapper.findAll(example)
    }

    override suspend fun queryAll(): List<EventLog> {
        return logMapper.findAll()
//        return logMapper.findAll().toList()
    }
}