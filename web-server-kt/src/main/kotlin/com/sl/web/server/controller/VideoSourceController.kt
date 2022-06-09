package com.sl.web.server.controller

import com.sl.web.server.dto.VideoSourceDto
import com.sl.web.server.email.EmailSender
import com.sl.web.server.email.generateAlarmEmail
import com.sl.web.server.entity.event_log
import com.sl.web.server.entity.Record
import com.sl.web.server.response.Wrapper
import com.sl.web.server.response.wrapper
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.VideoSourceService
import com.sl.web.server.utils.EventLogBuilder
import com.sl.web.server.utils.toDateTime
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/projects")
class VideoSourceController : BasicController() {

    @Autowired
    lateinit var videoSourceService: VideoSourceService

    @PostMapping("/create")
    suspend fun createVideoSource(
        @RequestBody(required = true) vsDto: VideoSourceDto
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(vsDto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(vsDto.token)!!.first
            val project =
                videoSourceService.insert(userId, vsDto.url, vsDto.projectName) ?:
                return@withContext "".wrapper(
                    203,
                    "创建失败"
                )
            val user = userService.query(setOf(userId))[0]
            EventLogBuilder()
                .setCreateTime(System.currentTimeMillis())
                .setType(event_log.Type.ProjectCreate)
                .setResourceId(project.resource_id)
                .setContent("project [${project.resource_id}:${project.name}] create")
                .saveTo(user)
            userService.update(user)
            "".wrapper(200, "创建成功")
        }
    }

    @PostMapping("/delete")
    suspend fun deleteProject(
        @RequestBody(required = true) dto: VideoSourceDto
    ):Wrapper<Any>{
        return withContext(coroutineContext){
            if (!TokenManager.validationToken(dto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(dto.token)!!.first
            val list = videoSourceService.queryByUserId(userId)
            val project = list.find { it.resource_id == dto.resourceId }
                ?: return@withContext "".wrapper(201, "删除失败")
            videoSourceService.delete(userId,project)
            "".wrapper(200,"删除成功")
        }
    }

    @PostMapping("/query_projects")
    suspend fun queryProjects(
        @RequestBody(required = true) dto: VideoSourceDto
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(dto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(dto.token)!!.first
            // fixed 这里只能查到自己创建的项目，应该是拿到所有与自己相关的项目
            videoSourceService.queryByUserId(userId)
                .wrapper(200, "查询成功")
        }
    }

    @PostMapping("/query_project")
    suspend fun queryProject(
        @RequestBody(required = true) dto: VideoSourceDto
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(dto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(dto.token)!!.first
            // fixed 这里只能查到自己创建的项目，应该是拿到所有与自己相关的项目
            videoSourceService.query(userId, dto.resourceId)?.wrapper(200, "查询成功") ?: return@withContext "".wrapper(
                201,
                "资源访问错误"
            )
        }
    }

    @PostMapping("/add_member")
    suspend fun addMembers(
        @RequestBody(required = true) vsDto: VideoSourceDto
    ): Wrapper<Any?> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(vsDto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(vsDto.token)!!.first
            val count = videoSourceService.addMember(vsDto.resourceId, userId, vsDto.members)
            if (count <= 0 || count != vsDto.members.size)
                return@withContext "".wrapper(201, "添加失败")
            else {
                val user = userService.query(setOf(userId))[0]
                EventLogBuilder()
                    .setCreateTime(System.currentTimeMillis())
                    .setType(event_log.Type.ProjectAddMember)
                    .setResourceId(vsDto.resourceId)
                    .setContent("project [${vsDto.resourceId}:${vsDto.projectName}] add members ${vsDto.members}")
                    .saveTo(user)
                userService.update(user)
                videoSourceService.queryByProjectId(vsDto.resourceId).wrapper(200, "添加成功")
            }
        }
    }

    @PostMapping("/remove_member")
    suspend fun removeMembers(
        @RequestBody(required = true) vsDto: VideoSourceDto
    ): Wrapper<Any?> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(vsDto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(vsDto.token)!!.first
            val count = videoSourceService.removeMember(vsDto.resourceId, userId, vsDto.members)
            if (count <= 0 || count != vsDto.members.size)
                "".wrapper(201, "操作失败")
            else {
                val user = userService.query(setOf(userId))[0]
                EventLogBuilder()
                    .setCreateTime(System.currentTimeMillis())
                    .setType(event_log.Type.ProjectRemoveMember)
                    .setResourceId(vsDto.resourceId)
                    .setContent("project [${vsDto.resourceId}:${vsDto.projectName}] remove members ${vsDto.members}")
                    .saveTo(user)
                userService.update(user)
                videoSourceService.queryByProjectId(vsDto.resourceId)
                    .wrapper(200, "删除成功")
            }

        }
    }


    @PostMapping("/query_record")
    suspend fun queryRecord(
        @RequestBody(required = true) vsDto: VideoSourceDto
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(vsDto.token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(vsDto.token)!!.first
            val videoSource =
                videoSourceService.query(userId, vsDto.resourceId) ?: return@withContext "".wrapper(302, "查询失败")
            if (userId != videoSource.owner && !videoSource.members.map { it.user_id }.contains(userId))
                return@withContext "".wrapper(303, "无权限访问资源")
            val records = videoSource.records
            records.wrapper(200, "查询成功")
        }
    }

    @PostMapping("/report")
    suspend fun actionReport(
        @RequestBody(required = true) vsDto: VideoSourceDto
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            val userId = checkTokenAndBackID(vsDto.token) { return@withContext "".wrapper(101, "token失效") }
            val videoSource = videoSourceService.query(userId, vsDto.resourceId)
                ?: return@withContext "".wrapper(201, "资源错误")
            val members = videoSource.members
            val userList = userService.query(members.map { it.user_id }.toSet().plus(userId))
            val emailList = ArrayList<String>()
            emailList.addAll(userList.map { it.email })
            val time = Date(vsDto.notifyTime).time
            val maxLevel = getLevel(vsDto.actionCodes).maxOf { it }
            if (vsDto.needEmail) {
                when (maxLevel) {
                    2 -> {
                        EmailSender.Builder().init()
                            .setReceiver(emailList.toTypedArray()).setTitle("检测到危险行为")
                            .setContent(generateAlarmEmail(vsDto.url, Date(time).toDateTime()))
                            .build().send()
                    }
                }
            }
            val record = Record()
            record.action_code = maxLevel
            record.create_time = vsDto.notifyTime
            videoSource.records = videoSource.records.plus(record)
            val count = videoSourceService.update(videoSource)
            if (count != 1)
                return@withContext "".wrapper(201, "记录失败")
            val user = userService.query(setOf(userId))[0]
            EventLogBuilder()
                .setResourceId(videoSource.resource_id)
                .setType(event_log.Type.Alarm)
                .setContent("project [${vsDto.resourceId}:${videoSource.name}] - action codes:${vsDto.actionCodes} - level top:$maxLevel")
                .setCreateTime(time)
                .saveTo(user)
            if (userService.update(user))
                "".wrapper(200, "上报成功")
            else
                "".wrapper(201, "上报失败")
        }

    }


    /**
     * 其他动作都当作无危险行为
     */
    private val levelTwo = arrayOf(4/*摔倒*/, 6/*跳跃*/)
    private val levelThree = arrayOf(51/*射击*/, 57/*扔东西*/, 63 /*打架*/)

    private fun getLevel(actionCodes: List<Int>): Array<Int> {
        val levels = Array<Int>(actionCodes.size) { 0 }
        actionCodes.forEachIndexed { index, i ->
            if (levelThree.contains(i))
                levels[index] = 2
            else if (levelTwo.contains(i))
                levels[index] = 1
            else
                levels[index] = 0
        }
        return levels
    }
}