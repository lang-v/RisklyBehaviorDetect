package com.sl.web.server.mapper

import com.sl.web.server.entity.InputSource
import org.springframework.data.jpa.repository.JpaRepository

interface InputResourceMapper:JpaRepository<InputSource,Int>