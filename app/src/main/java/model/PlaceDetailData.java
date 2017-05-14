package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by muvi on 12/5/17.
 */

public class PlaceDetailData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("plcae_userid")
    @Expose
    private String plcaeUserid;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("place_name_de")
    @Expose
    private String placeNameDe;

    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("food")
    @Expose
    private String food;
    @SerializedName("gatearea_id")
    @Expose
    private String gateareaId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("descp")
    @Expose
    private String descp;
    @SerializedName("open_mon")
    @Expose
    private String openMon;
    @SerializedName("close_mon")
    @Expose
    private String closeMon;
    @SerializedName("mon")
    @Expose
    private String mon;
    @SerializedName("per_selection_start")
    @Expose
    private String perSelectionStart;
    @SerializedName("per_selection_end")
    @Expose
    private String perSelectionEnd;
    @SerializedName("barrier")
    @Expose
    private String barrier;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("open_tue")
    @Expose
    private String openTue;
    @SerializedName("close_tue")
    @Expose
    private String closeTue;
    @SerializedName("tue")
    @Expose
    private String tue;
    @SerializedName("open_wed")
    @Expose
    private String openWed;
    @SerializedName("close_wed")
    @Expose
    private String closeWed;
    @SerializedName("wed")
    @Expose
    private String wed;
    @SerializedName("open_thru")
    @Expose
    private String openThru;
    @SerializedName("close_thru")
    @Expose
    private String closeThru;
    @SerializedName("thru")
    @Expose
    private String thru;
    @SerializedName("open_fri")
    @Expose
    private String openFri;
    @SerializedName("close_fri")
    @Expose
    private String closeFri;
    @SerializedName("fri")
    @Expose
    private String fri;
    @SerializedName("open_sat")
    @Expose
    private String openSat;
    @SerializedName("close_sat")
    @Expose
    private String closeSat;
    @SerializedName("sat")
    @Expose
    private String sat;
    @SerializedName("open_sun")
    @Expose
    private String openSun;
    @SerializedName("close_sun")
    @Expose
    private String closeSun;
    @SerializedName("sun")
    @Expose
    private String sun;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("place_gallery")
    @Expose
    private ArrayList<Gallery> placeGallery;
    @SerializedName("gate_data")
    @Expose
    private GateModel gateData;
    @SerializedName("events_list")
    @Expose
    private ArrayList<EventModel> eventsList = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlcaeUserid() {
        return plcaeUserid;
    }

    public void setPlcaeUserid(String plcaeUserid) {
        this.plcaeUserid = plcaeUserid;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceNameDe() {
        return placeNameDe;
    }

    public void setPlaceNameDe(String placeNameDe) {
        this.placeNameDe = placeNameDe;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getGateareaId() {
        return gateareaId;
    }

    public void setGateareaId(String gateareaId) {
        this.gateareaId = gateareaId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
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

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getPerSelectionStart() {
        return perSelectionStart;
    }

    public void setPerSelectionStart(String perSelectionStart) {
        this.perSelectionStart = perSelectionStart;
    }

    public String getPerSelectionEnd() {
        return perSelectionEnd;
    }

    public void setPerSelectionEnd(String perSelectionEnd) {
        this.perSelectionEnd = perSelectionEnd;
    }

    public String getBarrier() {
        return barrier;
    }

    public void setBarrier(String barrier) {
        this.barrier = barrier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenTue() {
        return openTue;
    }

    public void setOpenTue(String openTue) {
        this.openTue = openTue;
    }

    public String getCloseTue() {
        return closeTue;
    }

    public void setCloseTue(String closeTue) {
        this.closeTue = closeTue;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getOpenWed() {
        return openWed;
    }

    public void setOpenWed(String openWed) {
        this.openWed = openWed;
    }

    public String getCloseWed() {
        return closeWed;
    }

    public void setCloseWed(String closeWed) {
        this.closeWed = closeWed;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getOpenThru() {
        return openThru;
    }

    public void setOpenThru(String openThru) {
        this.openThru = openThru;
    }

    public String getCloseThru() {
        return closeThru;
    }

    public void setCloseThru(String closeThru) {
        this.closeThru = closeThru;
    }

    public String getThru() {
        return thru;
    }

    public void setThru(String thru) {
        this.thru = thru;
    }

    public String getOpenFri() {
        return openFri;
    }

    public void setOpenFri(String openFri) {
        this.openFri = openFri;
    }

    public String getCloseFri() {
        return closeFri;
    }

    public void setCloseFri(String closeFri) {
        this.closeFri = closeFri;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getOpenSat() {
        return openSat;
    }

    public void setOpenSat(String openSat) {
        this.openSat = openSat;
    }

    public String getCloseSat() {
        return closeSat;
    }

    public void setCloseSat(String closeSat) {
        this.closeSat = closeSat;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getOpenSun() {
        return openSun;
    }

    public void setOpenSun(String openSun) {
        this.openSun = openSun;
    }

    public String getCloseSun() {
        return closeSun;
    }

    public void setCloseSun(String closeSun) {
        this.closeSun = closeSun;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Gallery> getPlaceGallery() {
        return placeGallery;
    }

    public void setPlaceGallery(ArrayList<Gallery> placeGallery) {
        this.placeGallery = placeGallery;
    }

    public GateModel getGateData() {
        return gateData;
    }

    public void setGateData(GateModel gateData) {
        this.gateData = gateData;
    }

    public ArrayList<EventModel> getEventsList() {
        return eventsList;
    }

    public void setEventsList(ArrayList<EventModel> eventsList) {
        this.eventsList = eventsList;
    }
}
