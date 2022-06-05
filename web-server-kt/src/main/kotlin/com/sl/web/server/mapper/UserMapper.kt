package com.sl.web.server.mapper

import com.sl.web.server.entity.user_info
import org.springframework.data.jpa.repository.JpaRepository

interface UserMapper : JpaRepository<user_info, String> {}