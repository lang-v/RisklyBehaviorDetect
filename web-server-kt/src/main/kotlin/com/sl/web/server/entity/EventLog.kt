package com.sl.web.server.entity

import javax.persistence.*

@Entity
@Table(name="log_info")
class EventLog {
    @Id
    @Column(name = "log_id",nullable = false)
    var logId = -1

    @Column(name = "user_id")
    lateinit var userId:String

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    lateinit var type:Type

    @Column(name = "create_time")
    var createTime = 0L

    @Column(name = "content")
    lateinit var content:String

    enum class Type{Login,Register,Reset,Update,Alarm,Other}
}