package com.sl.web.server.dto

import com.sl.web.server.entity.EventLog

class EventLogDto {
    var token = ""
    var type = EventLog.Type.All

    /**
     * 初步估计这个查询比较耗时
     * json { "start":0L, "end":1L }
     */
    var timeRange:LongRange? = null
}