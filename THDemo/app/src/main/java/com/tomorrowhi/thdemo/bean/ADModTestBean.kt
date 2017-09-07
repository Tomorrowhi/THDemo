package com.tomorrowhi.thdemo.bean

class ADModTestBean {

    var id: Int = 0
    lateinit var desc: String

    constructor(id: Int, desc: String) {
        this.id = id
        this.desc = desc
    }


    override fun toString(): String {
        return "ADModTestBean(id=$id, desc='$desc')"
    }


}