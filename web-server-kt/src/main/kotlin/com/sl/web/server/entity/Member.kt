package com.sl.web.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name ="project_member")
class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    var member_id = 0

    @JsonIgnore
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "resource_id")
    lateinit var source:VideoSource

    @Column(name = "user_id")
    lateinit var user_id:String
}