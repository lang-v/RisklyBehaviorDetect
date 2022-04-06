package com.sl.web.server.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "project_record")
class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    var record_id = 0

    @JsonIgnore
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
//    @JoinColumn(name = "project1",foreignKey = ForeignKey(name = "test1"))
    var source: Project?=null


    @Column(name = "time")
    var time = 0L

    @Column(name = "action_code")
    var action_code = 0

}