package com.sl.web.server.mapper

import com.sl.web.server.entity.EventLog
import org.springframework.data.jpa.repository.JpaRepository

interface LogMapper: JpaRepository<EventLog, Int> {}