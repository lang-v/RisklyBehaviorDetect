package com.sl.web.server.utils

import com.sl.web.server.entity.EventLog
import com.sl.web.server.entity.User

class EventLogBuilder {
    private val eventLog = EventLog()

    init {
        eventLog.resourceId = -1
    }

    fun setResourceId(id:Int) :EventLogBuilder{
        eventLog.resourceId = id
        return this
    }

    fun setType(type:EventLog.Type):EventLogBuilder{
        eventLog.type = type
        return this
    }

    fun setCreateTime(time:Long):EventLogBuilder{
        eventLog.createTime = time
        return this
    }

    fun setContent(content:String):EventLogBuilder{
        eventLog.content = content
        return this
    }

    fun get():EventLog{
        return eventLog
    }

    fun saveTo(user: User):User {
        eventLog.user = user
        user.events = user.events.plus(eventLog)
        return user
    }
}