package com.sl.web.server.service.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValidationCodeServiceImplTest{


    @Test
    fun testGenerateCode(){

        val service = ValidationCodeServiceImpl()
        val code = service.generateCode()
        val regex = Regex("""[\d]{6}""")
        assert(code.matches(regex))
    }
}