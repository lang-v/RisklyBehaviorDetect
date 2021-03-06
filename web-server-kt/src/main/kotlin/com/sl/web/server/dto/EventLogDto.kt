package com.sl.web.server.dto

import com.sl.web.server.entity.event_log

class EventLogDto {
    var token = ""
    var type :Set<event_log.Type> = setOf()

    /**
     * 初步估计这个查询比较耗时
     * json { "start":0L, "end":1L }
     */
    var timeRange:LongRange? = null
}