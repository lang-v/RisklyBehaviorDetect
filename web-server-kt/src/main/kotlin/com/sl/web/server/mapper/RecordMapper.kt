package com.sl.web.server.mapper

import com.sl.web.server.entity.Record
import org.springframework.data.jpa.repository.JpaRepository

interface RecordMapper:JpaRepository<Record,Int> {
}