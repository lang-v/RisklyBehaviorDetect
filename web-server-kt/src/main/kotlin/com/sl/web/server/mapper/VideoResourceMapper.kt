package com.sl.web.server.mapper

import com.sl.web.server.entity.VideoSource
import org.springframework.data.jpa.repository.JpaRepository

interface VideoResourceMapper:JpaRepository<VideoSource,Int>