package com.sl.web.server.service.impl

import com.sl.web.server.entity.Log
import com.sl.web.server.mapper.LogMapper
import com.sl.web.server.service.LogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class LogServiceImpl : LogService {

    @Autowired
    lateinit var logMapper: LogMapper

    override suspend fun queryByType(type: Log.Type): List<Log> {
        val log = Log()
        log.type = type
        val example = Example.of(log, ExampleMatcher.matchingAny().withIgnoreNullValues())
        return logMapper.findAll(example)
    }

    override suspend fun queryAll(): List<Log> {
        return logMapper.findAll()
//        return logMapper.findAll().toList()
    }
}