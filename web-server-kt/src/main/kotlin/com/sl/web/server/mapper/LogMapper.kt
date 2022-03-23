package com.sl.web.server.mapper

import com.sl.web.server.entity.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface LogMapper: JpaRepository<Log, Int> {}