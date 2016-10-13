package com.rilin.lzy.mybase.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzy on 16/10/8.
 */
public class CityView implements Serializable {
    private String cityid;

    private String cityname;

    private String provinceid;

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

    private List<CountyView> countylist;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid == null ? "" : cityid.trim();
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname == null ? "" : cityname.trim();
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid == null ? "" : provinceid.trim();
    }

    public List<CountyView> getCountylist() {
        return countylist;
    }

    public void setCountylist(List<CountyView> countylist) {
        this.countylist = countylist;
    }


}
