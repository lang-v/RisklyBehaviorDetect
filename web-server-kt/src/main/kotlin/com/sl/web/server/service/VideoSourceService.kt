package com.sl.web.server.service

import com.sl.web.server.entity.VideoSource

interface VideoSourceService {

    suspend fun insert(userId: String, source: String, projectName:String): VideoSource

    suspend fun addMember(resourceId: Int, owner: String, userIds: Set<String>): Int

    suspend fun removeMember(resourceId: Int, owner: String, userIds: Set<String>): Int

    suspend fun query(resourceId: Int): VideoSource?

    suspend fun queryByUserId(userId:String):List<VideoSource>

    suspend fun update(source: VideoSource):Int
}