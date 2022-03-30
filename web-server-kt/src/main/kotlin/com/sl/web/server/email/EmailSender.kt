package com.sl.web.server.email

import com.sun.mail.util.MailSSLSocketFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import java.security.GeneralSecurityException
import java.security.NoSuchProviderException
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class EmailSender {

    //发件人信息
    private val From = "1844977240@qq.com"

    //发件人邮箱
    private val recipient = "1844977240@qq.com"

    //邮箱密码
    private val password = "mphexpgiuugyebcb"

    //邮件发送的服务器
    private val host = "smtp.qq.com"


    private lateinit var properties:Properties
    private lateinit var session:Session
    private lateinit var mimeMessage: MimeMessage

    private fun init() {
        properties = Properties()
        properties.setProperty("mail.host", "smtp.qq.com")
        properties.setProperty("mail.transport.protocol", "smtp")
        properties.setProperty("mail.smtp.auth", "true")

        //QQ存在一个特性设置SSL加密
        var sf: MailSSLSocketFactory? = null
        try {
            sf = MailSSLSocketFactory()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
        sf?.isTrustAllHosts = true
        properties["mail.smtp.ssl.enable"] = "true"
        properties["mail.smtp.ssl.socketFactory"] = sf
        session = Session.getInstance(properties)
        session.debug = true
        mimeMessage = MimeMessage(session)
    }

    fun setTitle(title:String):EmailSender{
        mimeMessage.subject = title
        return this
    }

    fun setContent(content: String):EmailSender{
        mimeMessage.setContent(content,"text/html;charset=UTF-8")
        return this
    }

    fun setReceiver(receivers:Array<String>):EmailSender{
        val ads = receivers.map { InternetAddress(it) }.toTypedArray()
        mimeMessage.setRecipients(Message.RecipientType.TO, ads)
        return this
    }

    fun build():EmailSender{
        mimeMessage.setFrom(InternetAddress(recipient))

        return this
    }

    suspend fun send():Boolean{
        //获取连接对象
        var transport: Transport? = null
        try {
            transport = session.transport
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        }
        transport?:return false

        //连接服务器
        transport.connect(host, From, password)

        //发送邮件
        transport.sendMessage(mimeMessage, mimeMessage.allRecipients)
        transport.close()
        return true
    }

    class Builder{
        private val sender = EmailSender()
        fun init():EmailSender {
            sender.init()
            return sender
        }
    }
}

