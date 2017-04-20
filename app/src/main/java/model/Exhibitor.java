package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IMFCORP\alok.acharya on 17/4/17.
 */

public class Exhibitor {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("descp")
    @Expose
    private String descp;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("open_mon")
    @Expose
    private String openMon;
    @SerializedName("close_mon")
    @Expose
    private String closeMon;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("header_pic")
    @Expose
    private String headerPic;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SerializedName("longitude")
    @Expose

    private String longitude;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenMon() {
        return openMon;
    }

    public void setOpenMon(String openMon) {
        this.openMon = openMon;
    }

    public String getCloseMon() {
        return closeMon;
    }

    public void setCloseMon(String closeMon) {
        this.closeMon = closeMon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeaderPic() {
        return headerPic;
    }

    public void setHeaderPic(String headerPic) {
        this.headerPic = headerPic;
    }

}
