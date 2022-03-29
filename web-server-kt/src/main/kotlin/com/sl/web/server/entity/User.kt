package com.sl.web.server.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name="user_info")
class User{

    @Id
    @Column(name = "user_id",updatable = false)
    var userId: String = ""

    @Column(name = "email")
    var email = ""

    @Column(name = "username")
    var username = ""

    @Column(name = "password")
    var password = ""

    @Column(name = "token")
    var token = ""
}
