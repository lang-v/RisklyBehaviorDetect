package com.sl.web.server.service.impl

import com.sl.web.server.entity.EventLog
import com.sl.web.server.entity.User
import com.sl.web.server.mapper.LogMapper
import com.sl.web.server.mapper.UserMapper
import com.sl.web.server.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogServiceImpl : LogService {

    @Autowired
    lateinit var logMapper: LogMapper

    @Autowired
    lateinit var userMapper:UserMapper

    override suspend fun insert(log: EventLog): Int {
        logMapper.saveAndFlush(log)
        return 1
    }

    override suspend fun queryByFilter(userId:String ,type: EventLog.Type, timeRange: LongRange?): List<EventLog> {
        val user = userMapper.findById(userId).let {
            if (it.isPresent) it.get() else return listOf()
        }

        return if (type == EventLog.Type.All) {
            if (timeRange != null)
                return user.events.filter { it.createTime in timeRange }.toList()
            user.events.toList()
        }else{
            val list = user.events.filter { it.type == type }.toList()
            if (timeRange == null) list else list.filter { it.createTime in timeRange }
        }
    }

    override suspend fun queryAll(userId:String): List<EventLog> {
        val eventLog = EventLog()
//        eventLog.user.userId = userId
//        eventLog.user.userId = userId
        val example = Example.of(eventLog, ExampleMatcher.matchingAny())
        return logMapper.findAll(example)
    }
}