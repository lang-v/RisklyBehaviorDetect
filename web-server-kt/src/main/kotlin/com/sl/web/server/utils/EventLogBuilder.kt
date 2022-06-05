package com.sl.web.server.utils

import com.sl.web.server.entity.event_log
import com.sl.web.server.entity.user_info

class EventLogBuilder {
    private val eventLog = event_log()

    init {
        eventLog.resource_id = -1
    }

    fun setResourceId(id:Int) :EventLogBuilder{
        eventLog.resource_id = id
        return this
    }

    fun setType(type:event_log.Type):EventLogBuilder{
        eventLog.type = type
        return this
    }

    fun setCreateTime(time:Long):EventLogBuilder{
        eventLog.create_time = time
        return this
    }

    fun setContent(content:String):EventLogBuilder{
        eventLog.content = content
        return this
    }

    fun get():event_log{
        return eventLog
    }

    fun saveTo(user: user_info):user_info {
        eventLog.user = user
        user.events = user.events.plus(eventLog)
        return user
    }
}