package com.sl.web.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "log_info")
class event_log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    var log_id = 0

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: Type = Type.All

    @Column(name = "create_time")
    var create_time = 0L

    @Column(name = "resource_id")
    var resource_id = -1

    @Column(name = "content")
    var content: String = ""

    @JsonIgnore
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    var user: user_info?=null

    enum class Type {
        Login, Register, Reset, Update, ProjectCreate,
        ProjectAddMember, ProjectRemoveMember, Alarm, All
    }
}