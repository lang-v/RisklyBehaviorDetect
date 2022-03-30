package com.sl.web.server.service.impl

import com.sl.web.server.entity.ValidationCode
import com.sl.web.server.service.ValidationCodeService
import com.sl.web.server.utils.toDateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

internal class ValidationCodeServiceImplTest {

//    @Autowired
//    var validationCodeService = ValidationCodeServiceImpl()

    @Test
    fun testGenerateCode() {

        val service = ValidationCodeServiceImpl()
        val code = service.generateCode()
        val regex = Regex("""[\d]{6}""")
        assert(code.matches(regex))
    }

    @Test
    fun testSql() {
//        val validationCodeService = ValidationCodeServiceImpl()
        val bean = ValidationCode()
        bean.email = "ws2240@qq.com"
        bean.userId = "frontman"
        bean.lastApplyTime = Date().time

        val newDate = Date(bean.lastApplyTime).time
//        bean.code = validationCodeService.generateCode()

//        validationCodeService.saveOrUpdate(bean)
    }
}