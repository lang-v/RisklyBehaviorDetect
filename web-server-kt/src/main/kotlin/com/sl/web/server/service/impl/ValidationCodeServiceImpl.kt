package com.sl.web.server.service.impl

import com.sl.web.server.entity.ValidationCode
import com.sl.web.server.mapper.ValidationCodeMapper
import com.sl.web.server.service.ValidationCodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.util.*

@Service
class ValidationCodeServiceImpl:ValidationCodeService {

    @Autowired
    lateinit var validationCodeMapper:ValidationCodeMapper

    override fun query(userId: String): ValidationCode? {
        val bean = ValidationCode()
        bean.userId = userId
        val example = Example.of(bean, ExampleMatcher.matchingAny())
        return validationCodeMapper.findOne(example).let {
            if (it.isPresent) it.get() else null
        }
    }

    override fun saveOrUpdate(bean:ValidationCode): Int {
        bean.lastApplyTime = Date().toString()
        validationCodeMapper.saveAndFlush(bean)
        return 1
    }

    override fun generateCode(): String {
        val codes = "0123456789"
        val builder = StringBuilder()
        repeat(6) {
            val char = Random().nextInt(codes.length)
            builder.append("$char")
        }
        return builder.toString()
    }
}