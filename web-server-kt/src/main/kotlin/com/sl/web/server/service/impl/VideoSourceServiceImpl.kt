package com.sl.web.server.service.impl

import com.sl.web.server.entity.Member
import com.sl.web.server.entity.VideoSource
import com.sl.web.server.mapper.VideoResourceMapper
import com.sl.web.server.service.VideoSourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class VideoSourceServiceImpl : VideoSourceService {

    @Autowired
    lateinit var sourceMapper: VideoResourceMapper

    override fun insert(userId: String, source: String): VideoSource {
        val videoSource = VideoSource()
        videoSource.createTime = Date().time
        videoSource.members = setOf()
        videoSource.type = if (source == "0") VideoSource.Type.Camera else VideoSource.Type.LocalFile
        videoSource.url = source
        return sourceMapper.saveAndFlush(videoSource)
    }

    override fun addMember(resourceId: Int, owner: String, userIds: Set<String>): Int {
        val videoSource = sourceMapper.findByIdOrNull(resourceId) ?: return 0
        if (videoSource.userId != owner) return 0
        if (videoSource.members.any { userIds.contains(it.user_id) })
        // 这种就是出现了重复成员
            return -1
        val memberWithId = userIds.map {
            Member().apply {
                this.user_id = it
                this.source = VideoSource().apply { this.resourceId = resourceId }
            }
        }.toSet()

        val newMembers = videoSource.members.plus(memberWithId)
        videoSource.members = newMembers
        sourceMapper.saveAndFlush(videoSource)
        return userIds.size
    }

    override fun removeMember(resourceId: Int, owner: String, userIds: Set<String>): Int {
        val videoSource = sourceMapper.findByIdOrNull(resourceId) ?: return 0
        if (videoSource.userId != owner) return 0
        if (!videoSource.members.map { it.user_id }.containsAll(userIds.toList()))
        // 出现了不存在的成员
            return -1
        val memberWithId = userIds.map {
            Member().apply {
                this.user_id = it
                this.source = VideoSource().apply { this.resourceId = resourceId }
            }
        }.toSet()
        val newMembers = videoSource.members.minus(memberWithId)
        videoSource.members = newMembers
        sourceMapper.saveAndFlush(videoSource)
        return userIds.size
    }

    override fun query(resourceId: Int): VideoSource? {
        return sourceMapper.findByIdOrNull(resourceId)
    }


}