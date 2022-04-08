package com.sl.web.server

import com.sl.web.server.code.Aes
import com.sl.web.server.utils.fromUnicode
import com.sl.web.server.utils.isLegal
import com.sl.web.server.utils.toUnicode
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WebServerKtApplicationTests {

    @Test
    fun contextLoads() {
    }


    @Test
    fun testAes_StringWithEmpty(){
//        val emptyChar = Char.MIN_VALUE
//        val origin = "测试${emptyChar}validation"
//        val key = "hhhh"
//        val code = Aes.encrypt(origin.toUnicode(),key.toCharArray())
//        val decode = Aes.decrypt(code,key.toCharArray()).fromUnicode()
//
//        println(origin)
//        println(decode)
//
//        assert(origin != decode)
    }

    @Test
    fun testIllegalUserId(){
        val emptyChar = Char.MIN_VALUE
        var origin = "测试${emptyChar}validation"
        assert(!origin.isLegal())

        origin = "useridnasduigi dsad()）—"
        assert(!origin.isLegal())

        origin = "我是合格ID12138"
        assert(origin.isLegal())
    }
}
