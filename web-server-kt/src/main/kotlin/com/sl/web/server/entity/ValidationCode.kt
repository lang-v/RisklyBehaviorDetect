package com.sl.web.server.entity

import javax.persistence.*

@Entity(name = "validation_code")
class ValidationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    var record_id = 0

    @Column(name = "user_id")
    @OneToOne
    var user_id: user_info? = null
//    var userId = ""

    @Column(name = "email")
    var email = ""

    @Column(name = "last_apply_time")
    var last_apply_time = 0L

    @Column(name = "code",length = 6)
    var code = ""

}