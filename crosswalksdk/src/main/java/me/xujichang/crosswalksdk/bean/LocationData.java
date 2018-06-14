package me.xujichang.crosswalksdk.bean;

/**
 * Des:
 *
 * @author xujichang
 * created at 2018/6/14 - 14:08
 */
public class LocationData {
    private double lat;
    private double lng;
    private String des;

    public LocationData(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
