package com.sl.web.server.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "validation_code")
class ValidationCode {

    @Id
    @Column(name = "record_id")
    var recordId = -1

    @Column(name = "user_id")
    var userId = ""

    @Column(name = "email")
    var email = ""

    @Column(name = "last_apply_time",columnDefinition = "datetime")
    var lastApplyTime = ""

    @Column(name = "code",length = 6)
    var code = ""

}