package com.tomorrowhi.thdemo.bean

class MainFunctionBean {
    var name: String = "";
    var id: Int = 0;

    constructor(name: String, id: Int) {
        this.name = name
        this.id = id
    }

    override fun toString(): String {
        return "MainFunctionBean(name='$name', id=$id)"
    }


}