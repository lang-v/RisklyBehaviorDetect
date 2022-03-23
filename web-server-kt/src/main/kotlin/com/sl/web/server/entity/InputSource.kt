package com.sl.web.server.entity

import javax.persistence.*

//@Table(name = "projects",schema = "RECORDS")
@Entity(name="projects")
class InputSource {

    @Id
    @Column(name ="resource_id")
    var resourceId = 0

    @Column(name="input_url")
    var url = ""

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    var type = Type.Camera

    @Column(name = "create_time",columnDefinition = "DateTime")
    var createTime = ""

    @Column(name="user_id")
    var owner = ""

    @JoinTable(name = "projects_members",joinColumns = [JoinColumn(name="resource_id")])
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name="members")
    lateinit var members:Map<Int,String>

    enum class Type{LocalFile,OnlineResource,Camera}
}