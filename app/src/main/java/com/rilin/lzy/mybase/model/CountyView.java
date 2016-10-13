package com.rilin.lzy.mybase.model;

import java.io.Serializable;

/**
 * Created by lzy on 16/10/8.
 */
public class CountyView implements Serializable {
    private String countyid;

    private String countyname;

    private String cityid;

    private String provinceid;

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    private String centerlat;

    private String centerlon;

    private String radius;

    public String getCenterlat() {
        return centerlat;
    }

    public void setCenterlat(String centerlat) {
        this.centerlat = centerlat;
    }

    public String getCenterlon() {
        return centerlon;
    }

    public void setCenterlon(String centerlon) {
        this.centerlon = centerlon;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid == null ? "" : countyid.trim();
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname == null ? "" : countyname.trim();
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }


}
