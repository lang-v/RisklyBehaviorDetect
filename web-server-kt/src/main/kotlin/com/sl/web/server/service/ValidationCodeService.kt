package com.sl.web.server.service

import com.sl.web.server.entity.ValidationCode


interface ValidationCodeService {

    fun query(userId:String): ValidationCode?

    fun saveOrUpdate(bean:ValidationCode):Int

    fun generateCode():String
}