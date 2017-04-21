package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alok on 21-04-2017.
 */
public class EventModel implements Serializable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;

    public String getTitle_de() {
        return title_de;
    }

    public void setTitle_de(String title_de) {
        this.title_de = title_de;
    }

    @SerializedName("title_de")
    @Expose
    private String title_de;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("speaker")
    @Expose
    private String speaker;
    @SerializedName("art_version")
    @Expose
    private String artVersion;
    @SerializedName("program_theme")
    @Expose
    private String programTheme;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("descp")
    @Expose
    private String descp;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("admittance")
    @Expose
    private String admittance;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("event_suit")
    @Expose
    private String eventSuit;
    @SerializedName("return_no")
    @Expose
    private String returnNo;
    @SerializedName("return_daily")
    @Expose
    private String returnDaily;
    @SerializedName("return_weekly")
    @Expose
    private String returnWeekly;
    @SerializedName("return_ddaily")
    @Expose
    private String returnDdaily;
    @SerializedName("return_monthly")
    @Expose
    private String returnMonthly;
    @SerializedName("return_sun")
    @Expose
    private String returnSun;
    @SerializedName("return_mon")
    @Expose
    private String returnMon;
    @SerializedName("return_tue")
    @Expose
    private String returnTue;
    @SerializedName("return_wed")
    @Expose
    private String returnWed;
    @SerializedName("return_thru")
    @Expose
    private String returnThru;
    @SerializedName("return_fri")
    @Expose
    private String returnFri;
    @SerializedName("return_sat")
    @Expose
    private String returnSat;
    @SerializedName("date_return")
    @Expose
    private String dateReturn;
    @SerializedName("adddesc")
    @Expose
    private String adddesc;
    @SerializedName("ticket")
    @Expose
    private String ticket;
    @SerializedName("ticket_price")
    @Expose
    private String ticketPrice;
    @SerializedName("tick_information")
    @Expose
    private String tickInformation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("gatearea_id")
    @Expose
    private String gateareaId;
    @SerializedName("place_id")
    @Expose
    private String placeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getArtVersion() {
        return artVersion;
    }

    public void setArtVersion(String artVersion) {
        this.artVersion = artVersion;
    }

    public String getProgramTheme() {
        return programTheme;
    }

    public void setProgramTheme(String programTheme) {
        this.programTheme = programTheme;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdmittance() {
        return admittance;
    }

    public void setAdmittance(String admittance) {
        this.admittance = admittance;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getEventSuit() {
        return eventSuit;
    }

    public void setEventSuit(String eventSuit) {
        this.eventSuit = eventSuit;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public String getReturnDaily() {
        return returnDaily;
    }

    public void setReturnDaily(String returnDaily) {
        this.returnDaily = returnDaily;
    }

    public String getReturnWeekly() {
        return returnWeekly;
    }

    public void setReturnWeekly(String returnWeekly) {
        this.returnWeekly = returnWeekly;
    }

    public String getReturnDdaily() {
        return returnDdaily;
    }

    public void setReturnDdaily(String returnDdaily) {
        this.returnDdaily = returnDdaily;
    }

    public String getReturnMonthly() {
        return returnMonthly;
    }

    public void setReturnMonthly(String returnMonthly) {
        this.returnMonthly = returnMonthly;
    }

    public String getReturnSun() {
        return returnSun;
    }

    public void setReturnSun(String returnSun) {
        this.returnSun = returnSun;
    }

    public String getReturnMon() {
        return returnMon;
    }

    public void setReturnMon(String returnMon) {
        this.returnMon = returnMon;
    }

    public String getReturnTue() {
        return returnTue;
    }

    public void setReturnTue(String returnTue) {
        this.returnTue = returnTue;
    }

    public String getReturnWed() {
        return returnWed;
    }

    public void setReturnWed(String returnWed) {
        this.returnWed = returnWed;
    }

    public String getReturnThru() {
        return returnThru;
    }

    public void setReturnThru(String returnThru) {
        this.returnThru = returnThru;
    }

    public String getReturnFri() {
        return returnFri;
    }

    public void setReturnFri(String returnFri) {
        this.returnFri = returnFri;
    }

    public String getReturnSat() {
        return returnSat;
    }

    public void setReturnSat(String returnSat) {
        this.returnSat = returnSat;
    }

    public String getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(String dateReturn) {
        this.dateReturn = dateReturn;
    }

    public String getAdddesc() {
        return adddesc;
    }

    public void setAdddesc(String adddesc) {
        this.adddesc = adddesc;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTickInformation() {
        return tickInformation;
    }

    public void setTickInformation(String tickInformation) {
        this.tickInformation = tickInformation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getGateareaId() {
        return gateareaId;
    }

    public void setGateareaId(String gateareaId) {
        this.gateareaId = gateareaId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
