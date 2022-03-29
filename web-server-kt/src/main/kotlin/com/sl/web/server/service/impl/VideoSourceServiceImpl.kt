package com.sl.web.server.service.impl

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
        videoSource.createTime = Date().toString()
        videoSource.members = setOf()
        videoSource.type = if (source == "0") VideoSource.Type.Camera else VideoSource.Type.LocalFile
        videoSource.url = source
        return sourceMapper.saveAndFlush(videoSource)
    }

    override fun addMember(resourceId: Int, owner: String, userIds: Set<String>): Int {
        val videoSource = sourceMapper.findByIdOrNull(resourceId) ?: return 0
        if (videoSource.userId != owner) return 0
        if (videoSource.members.any { userIds.contains(it) })
        // 这种就是出现了重复成员
            return -1
        val newMembers = videoSource.members.plus(userIds)
        videoSource.members = newMembers
        sourceMapper.saveAndFlush(videoSource)
        return userIds.size
    }

    override fun removeMember(resourceId: Int, owner: String, userIds: Set<String>): Int {
        val videoSource = sourceMapper.findByIdOrNull(resourceId) ?: return 0
        if (videoSource.userId != owner) return 0
        if (!videoSource.members.containsAll(userIds.toList()))
        // 出现了不存在的成员
            return -1
        val newMembers = videoSource.members.minus(userIds)
        videoSource.members = newMembers
        sourceMapper.saveAndFlush(videoSource)
        return userIds.size
    }

    override fun query(resourceId: Int): VideoSource? {
        return sourceMapper.findByIdOrNull(resourceId)
    }


}