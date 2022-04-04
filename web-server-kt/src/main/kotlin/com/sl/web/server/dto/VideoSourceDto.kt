package com.sl.web.server.dto

class VideoSourceDto {

    var resourceId = -1

    var projectName = ""

    var notifyTime = 0L

    var token = ""

    var url = ""

    var members:Set<String> = setOf()

    var actionCodes: List<Int> = listOf()

    var needEmail = false

}