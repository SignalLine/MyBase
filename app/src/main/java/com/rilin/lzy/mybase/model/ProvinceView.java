package com.rilin.lzy.mybase.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzy on 16/10/8.
 */
public class ProvinceView implements Serializable {
    private static final long serialVersionUID = 1L;

    private String provinceid;

    private String provincename;

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

    private List<CityView> citylist;

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid == null ? "" : provinceid.trim();
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename == null ? "" : provincename.trim();
    }

    public List<CityView> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CityView> citylist) {
        this.citylist = citylist;
    }


}
