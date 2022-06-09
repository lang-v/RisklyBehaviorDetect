package com.sl.web.server.service.impl

import com.sl.web.server.entity.Member
import com.sl.web.server.entity.Project
import com.sl.web.server.entity.user_info
import com.sl.web.server.mapper.MemberMapper
import com.sl.web.server.mapper.UserMapper
import com.sl.web.server.mapper.VideoResourceMapper
import com.sl.web.server.service.VideoSourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class VideoSourceServiceImpl : VideoSourceService {

    @Autowired
    lateinit var sourceMapper: VideoResourceMapper

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var memberMapper: MemberMapper

    override suspend fun insert(userId: String, source: String, projectName: String): Project? {
        val project = Project()
        project.name = projectName
        project.owner = userId
        project.create_time = Date().time
        project.members = setOf()
        project.type = if (source == "0") Project.Type.Camera else Project.Type.LocalFile
        project.url = source
        val user = userMapper.findByIdOrNull(userId) ?: return null
        project.user = user
        user.projects = user.projects.plus(project)
        userMapper.saveAndFlush(user)
        return project
    }

    override suspend fun addMember(resourceId: Int, owner: String, userIds: Set<String>): Int {
        val videoSource = sourceMapper.findByIdOrNull(resourceId) ?: return 0
        if (videoSource.owner != owner) return 0
        if (videoSource.members.any { userIds.contains(it.user_id) })
        // 这种就是出现了重复成员
            return -1
        val memberWithId = userIds.map {
            Member().apply {
                this.user_id = it
                this.source = videoSource
//                this.source = VideoSource().apply { this.resourceId = resourceId }
            }
        }.toSet()

        val newMembers = videoSource.members.plus(memberWithId)
        videoSource.members = newMembers
        sourceMapper.saveAndFlush(videoSource)
        return userIds.size
    }

    override suspend fun removeMember(resourceId: Int, owner: String, userIds: Set<String>): Int {
        val videoSource = sourceMapper.findByIdOrNull(resourceId) ?: return 0
        if (videoSource.owner != owner) return 0
        if (!videoSource.members.map { it.user_id }.containsAll(userIds.toList()))
        // 出现了不存在的成员
            return -1
        val newMembers = videoSource.members.filter { !userIds.contains(it.user_id) }.toSet()
        val count = videoSource.members.size - newMembers.size
        videoSource.members = newMembers
        sourceMapper.saveAndFlush(videoSource)
        return count
    }

    override suspend fun query(userId: String, resourceId: Int): Project? {
        val user = userMapper.findByIdOrNull(userId) ?: return null
        user.projects.forEach {
            if (it.resource_id == resourceId)
                return it
        }
        return null
    }

    override suspend fun queryByUserId(userId: String): List<Project> {
        val list = ArrayList<Project>()
        val p = Member().apply {
            this.user_id = userId
            this.source = null
        }
        val example = Example.of(p, ExampleMatcher.matchingAny().withIgnoreNullValues())
        val temp = memberMapper.findAll(example)
        list.addAll(sourceMapper.findAllById(temp.map { it.source?.resource_id }))
        val user = userMapper.findByIdOrNull(userId) ?: return list
        list.addAll(user.projects)
        return list
    }

    override suspend fun queryByProjectId(resourceId: Int): Project? {
        return sourceMapper.findByIdOrNull(resourceId)
    }

    override suspend fun update(source: Project): Int {
        sourceMapper.saveAndFlush(source)
        return 1
    }

    override suspend fun delete(userId: String,source: Project): Int {
        val user = userMapper.findById(userId).let {
            if (!it.isPresent)
                return 0
            it.get()
        }
        user.projects = user.projects.filter { it.resource_id!=source.resource_id }.toSet()
        userMapper.saveAndFlush(user)
        return 1
    }


}