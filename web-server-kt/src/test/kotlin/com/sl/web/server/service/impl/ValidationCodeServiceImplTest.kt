package com.sl.web.server.service.impl

import com.sl.web.server.entity.ValidationCode
import org.junit.jupiter.api.Test
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
        bean.user_id = "frontman"
        bean.last_apply_time = Date().time

        val newDate = Date(bean.last_apply_time).time
//        bean.code = validationCodeService.generateCode()

//        validationCodeService.saveOrUpdate(bean)
    }
}