package com.sl.web.server.mapper

import com.sl.web.server.entity.event_log
import org.springframework.data.jpa.repository.JpaRepository

interface LogMapper: JpaRepository<event_log, Int> {}