package com.sl.web.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


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

    @JsonIgnore
    @Column(name = "password")
    var password = ""

    @Column(name = "token")
    var token = ""

    @JsonIgnore
    @OneToMany(targetEntity = VideoSource::class,cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    lateinit var projects:Set<VideoSource>

    @JsonIgnore
    @OneToMany(targetEntity = EventLog::class, cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    lateinit var events:Set<EventLog>
}
