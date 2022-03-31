package com.sl.web.server.mapper

import com.sl.web.server.entity.Project
import org.springframework.data.jpa.repository.JpaRepository

interface VideoResourceMapper:JpaRepository<Project,Int>