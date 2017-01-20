package com.tomorrowhi.thdemo.bean;

/**
 * Created by zhaotaotao on 20/01/2017.
 */

public class AndroidApiTest {


    /**
     * ver : 1.5
     * name : Cupcake
     * api : API level 3
     */

    private String ver;
    private String name;
    private String api;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    @Override
    public String toString() {
        return "AndroidApiTest{" +
                "ver='" + ver + '\'' +
                ", name='" + name + '\'' +
                ", api='" + api + '\'' +
                '}';
    }
}
