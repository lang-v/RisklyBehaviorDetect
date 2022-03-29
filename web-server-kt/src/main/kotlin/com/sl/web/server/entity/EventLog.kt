package com.sl.web.server.entity

import javax.persistence.*

@Entity
@Table(name="log_info")
class EventLog {
    @Id
    @Column(name = "log_id",nullable = false)
    var logId = -1

    @Column(name = "user_id")
    var userId = ""

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type = Type.Other

    @Column(name = "create_time",columnDefinition = "Date")
    var createTime = ""

    @Column(name = "content")
    var content = ""

    enum class Type{Login,Register,Reset,Update,Alarm,Other}
}