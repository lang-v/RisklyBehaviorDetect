package com.sl.web.server.controller

import com.sl.web.server.email.EmailSender
import com.sl.web.server.email.generateAlarmEmail
import com.sl.web.server.entity.EventLog
import com.sl.web.server.response.Wrapper
import com.sl.web.server.response.wrapper
import com.sl.web.server.security.TokenManager
import com.sl.web.server.service.LogService
import com.sl.web.server.service.UserService
import com.sl.web.server.service.VideoSourceService
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

@RestController("/projects")
class VideoSourceController : BasicController() {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var videoSourceService: VideoSourceService

    @Autowired
    lateinit var logService: LogService


    @PostMapping("/create")
    suspend fun createVideoSource(
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) url: String
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(token)!!.first
            videoSourceService.insert(userId, url)
            "".wrapper(200, "创建成功")
        }
    }

    @PostMapping("/add_member")
    suspend fun addMembers(
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) resourceId: Int,
        @RequestBody(required = true) members: Set<String>
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(token)!!.first
            videoSourceService.addMember(resourceId, userId, members)
            "".wrapper(200, "创建成功")
        }
    }

    @PostMapping("/query_record")
    suspend fun queryRecord(
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) resourceId: Int,
    ): Wrapper<Any> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(token)!!.first
            val videoSource = videoSourceService.query(resourceId)?:return@withContext "".wrapper(302,"查询失败")
            if (userId != videoSource.userId && !videoSource.members.contains(userId))
                return@withContext "".wrapper(303, "无权限访问资源")
            val records = videoSource.record
            records.wrapper(200,"查询成功")
        }
    }

    @PostMapping("/remove_member")
    suspend fun removeMembers(
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) resourceId: Int,
        @RequestBody(required = true) members: Set<String>
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val userId = TokenManager.decodeToken(token)!!.first
            videoSourceService.removeMember(resourceId, userId, members)
            "".wrapper(200, "创建成功")
        }
    }


    @PostMapping("/report")
    suspend fun actionReport(
        @RequestBody(required = true) resourceId: Int,
        @RequestBody(required = true) token: String,
        @RequestBody(required = true) notifyTime: Long,
        @RequestBody(required = true) actionCodes: Set<Int>
    ): Wrapper<String> {
        return withContext(coroutineContext) {
            if (!TokenManager.validationToken(token, userService::checkId))
                return@withContext "".wrapper(101, "token失效")
            val videoSource = videoSourceService.query(resourceId)

            val members = videoSource!!.members
            val userList = userService.query(members)

            val emailList = ArrayList<String>()
            emailList.add(videoSource.userId)
            emailList.addAll(userList.map { it.email })

            val time = Date(notifyTime).toString()
            val maxLevel = getLevel(actionCodes).maxOf { it }

            when (maxLevel) {
                2 -> {
                    emailList.forEach {
                        // 挨个发邮件
                        EmailSender.Builder()
                            .init()
                            .setReceiver(it)
                            .setTitle("检测到危险行为")
                            .setContent(generateAlarmEmail("http://127.0.0.1:8080/index", time))
                    }
                }
            }
            // 写入日志
            val log = EventLog()
            log.createTime = time
            log.userId = videoSource.userId
            log.type = EventLog.Type.Alarm
            log.content = "action codes:$actionCodes - level top:$maxLevel"
            logService.insert(log)

            "".wrapper(200, "上报成功")
        }

    }


    /**
     * 其他动作都当作无危险行为
     */
    private val levelTwo = arrayOf(6/*跳跃*/)
    private val levelThree = arrayOf(51/*射击*/, 57/*扔东西*/, 63 /*打架*/)

    private fun getLevel(actionCodes: Set<Int>): Array<Int> {
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