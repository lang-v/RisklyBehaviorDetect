package com.sl.web.server.email

import javafx.scene.web.HTMLEditor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class EmailSenderTest{
    @Test
    fun testSendEmail(){
        GlobalScope.launch {
            EmailSender.Builder()
                .init()
                .setReceiver("shilangwork@163.com")
                .setTitle("html邮件测试")
                .setContent(generateVerificationCodeEmail("注册账户","776223"))
                .build()
                .send()
            EmailSender.Builder()
                .init()
                .setReceiver("shilangwork@163.com")
                .setTitle("html邮件测试")
                .setContent(generateResetLinkEmail("https:///www.baidu.com"))
                .build()
                .send()
        }

        while (true)
            Thread.yield()
    }
}