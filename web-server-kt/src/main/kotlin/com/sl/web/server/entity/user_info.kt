package com.sl.web.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
@Table(name="user_info")
class user_info{

    @Id
    @Column(name = "user_id",updatable = false,nullable = false)
    var user_id: String = ""

    @Column(name = "email",nullable = false)
    var email = ""

    @Column(name = "username")
    var username = ""

    @JsonIgnore
    @Column(name = "password",nullable = false)
    var password = ""

    @Column(name = "token")
    var token = ""

    @JsonIgnore
    @OneToMany(targetEntity = Project::class,cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    lateinit var projects:Set<Project>

    @JsonIgnore
    @OneToMany(targetEntity = event_log::class, cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    lateinit var events:Set<event_log>

    @JsonIgnore
    @OneToOne(targetEntity = ValidationCode::class,cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    lateinit var validationCode: ValidationCode
}
