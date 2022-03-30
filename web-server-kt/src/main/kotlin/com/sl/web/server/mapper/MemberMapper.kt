package com.sl.web.server.mapper

import com.sl.web.server.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberMapper:JpaRepository<Member,Int>