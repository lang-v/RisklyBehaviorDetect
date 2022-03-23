package com.sl.web.server.controller

import com.sl.web.server.response.wrapper
import com.sl.web.server.entity.Log.Type
import com.sl.web.server.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/log")
class LogController:BasicController() {

    @Autowired
    lateinit var logService: LogService

    @GetMapping("/all")
    suspend fun all() = logService.queryAll().wrapper(200,"")

    @GetMapping("/query")
    suspend fun queryByType(@RequestParam(name = "type") type:Type) = logService.queryByType(type).wrapper(200,"")
}