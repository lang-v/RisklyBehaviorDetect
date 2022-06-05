package com.sl.web.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "project")
class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "Int", nullable = false)
    var resource_id = 0

    @Column(name = "input_url")
    var url = ""

    @Column(name = "project_name")
    var name = ""

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type = Type.LocalFile

    @Column(name = "create_time")
    var create_time = 0L

    @Column(name = "owner")
    var owner = ""

    @JsonIgnore
    //    @Column(name = "user_id")
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    var user: user_info? = null

    //    @JsonIgnore
    //    @JoinTable(name = "project_members",joinColumns = [JoinColumn(name="resource_id")])
    @OneToMany(targetEntity = Member::class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "project_member",
//        joinColumns = [JoinColumn(name = "project_resource_id", referencedColumnName = "ID")],
//        inverseJoinColumns = [JoinColumn(name = "project_member_id", referencedColumnName = "ID")]
//    )
//    @JoinColumn(name = "source_member")
//    @JoinColumn(name = "videosource_id")
//    @Column(name = "member")
    var members: Set<Member> = setOf()

    //    @JsonIgnore
//    @JoinTable(
//        name = "project_record",
//        joinColumns = [JoinColumn(name = "project_resource_id", referencedColumnName = "ID")],
//        inverseJoinColumns = [JoinColumn(name = "project_record_id", referencedColumnName = "ID")]
//    )
//    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
//    @JoinColumn(name = "records")
    @OneToMany(targetEntity = Record::class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var records: Set<Record> = setOf()

    enum class Type { LocalFile, Camera }
}