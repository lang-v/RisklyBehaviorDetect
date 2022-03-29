package com.sl.web.server.entity

import org.hibernate.mapping.Join
import javax.persistence.*

@Entity(name="projects")
class VideoSource {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="resource_id", columnDefinition = "Long",nullable = false)
    var resourceId = 0

    @Column(name="input_url")
    var url = ""

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    var type = Type.LocalFile

    @Column(name = "create_time",columnDefinition = "DateTime")
    var createTime = ""

    @Column(name="user_id")
    var userId = ""

    @JoinTable(name = "projects_members",joinColumns = [JoinColumn(name="resource_id")])
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name="members")
    var members:Set<String> = setOf()

    @JoinTable(name = "project_records",joinColumns = [JoinColumn(name = "resource_id")])
    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "record")
    var record:Map<String,String> = mapOf()

    enum class Type{LocalFile,Camera}
}