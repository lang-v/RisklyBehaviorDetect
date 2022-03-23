package com.sl.web.server.mapper

import com.sl.web.server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserMapper : JpaRepository<User, String> {}