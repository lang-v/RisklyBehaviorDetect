package com.sl.web.server.entity

import javax.persistence.*

@Entity(name = "validation_code")
class ValidationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    var recordId = 0

    @Column(name = "user_id")
    var userId = ""

    @Column(name = "email")
    var email = ""

    @Column(name = "last_apply_time")
    var lastApplyTime = 0L

    @Column(name = "code",length = 6)
    var code = ""

}