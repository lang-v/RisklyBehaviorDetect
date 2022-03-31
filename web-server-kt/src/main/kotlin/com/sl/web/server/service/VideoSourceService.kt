package com.sl.web.server.service

import com.sl.web.server.entity.Project

interface VideoSourceService {

    suspend fun insert(userId: String, source: String, projectName:String): Int

    suspend fun addMember(resourceId: Int, owner: String, userIds: Set<String>): Int

    suspend fun removeMember(resourceId: Int, owner: String, userIds: Set<String>): Int

    suspend fun query(userId: String,resourceId: Int): Project?

    suspend fun queryByUserId(userId:String):List<Project>

    suspend fun update(source: Project):Int
}