package com.sl.web.server.mapper

import com.sl.web.server.entity.ValidationCode
import org.springframework.data.jpa.repository.JpaRepository

interface ValidationCodeMapper:JpaRepository<ValidationCode,Int>