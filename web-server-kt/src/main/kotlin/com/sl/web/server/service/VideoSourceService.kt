package com.sl.web.server.service

import com.sl.web.server.entity.VideoSource

interface VideoSourceService {

    fun insert(userId: String, source: String): VideoSource

    fun addMember(resourceId: Int, owner: String, userIds: Set<String>): Int

    fun removeMember(resourceId: Int, owner: String, userIds: Set<String>): Int

    fun query(resourceId: Int): VideoSource?

//    fun query()
}