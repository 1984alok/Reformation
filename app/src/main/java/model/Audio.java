package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alok on 15-04-2017.
 */
public class Audio {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title_en")
    @Expose
    private Object titleEn;
    @SerializedName("speaker_en")
    @Expose
    private String speakerEn;
    @SerializedName("chapter_en")
    @Expose
    private String chapterEn;
    @SerializedName("descp_en")
    @Expose
    private String descpEn;
    @SerializedName("gatearea_id")
    @Expose
    private String gateareaId;
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
    @SerializedName("audio_en")
    @Expose
    private String audioEn;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pimage")
    @Expose
    private String pimage;
    @SerializedName("audio_size_en")
    @Expose
    private String audioSizeEn;
    @SerializedName("place_latitude")
    @Expose
    private String placeLatitude;
    @SerializedName("place_longitude")
    @Expose
    private String placeLongitude;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("speaker")
    @Expose
    private String speaker;
    @SerializedName("chapter_de")
    @Expose
    private String chapterDe;
    @SerializedName("descp")
    @Expose
    private String descp;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("audio_size")
    @Expose
    private String audioSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(Object titleEn) {
        this.titleEn = titleEn;
    }

    public String getSpeakerEn() {
        return speakerEn;
    }

    public void setSpeakerEn(String speakerEn) {
        this.speakerEn = speakerEn;
    }

    public String getChapterEn() {
        return chapterEn;
    }

    public void setChapterEn(String chapterEn) {
        this.chapterEn = chapterEn;
    }

    public String getDescpEn() {
        return descpEn;
    }

    public void setDescpEn(String descpEn) {
        this.descpEn = descpEn;
    }

    public String getGateareaId() {
        return gateareaId;
    }

    public void setGateareaId(String gateareaId) {
        this.gateareaId = gateareaId;
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

    public String getAudioEn() {
        return audioEn;
    }

    public void setAudioEn(String audioEn) {
        this.audioEn = audioEn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getAudioSizeEn() {
        return audioSizeEn;
    }

    public void setAudioSizeEn(String audioSizeEn) {
        this.audioSizeEn = audioSizeEn;
    }

    public String getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(String placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public String getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(String placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getChapterDe() {
        return chapterDe;
    }

    public void setChapterDe(String chapterDe) {
        this.chapterDe = chapterDe;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudioSize() {
        return audioSize;
    }

    public void setAudioSize(String audioSize) {
        this.audioSize = audioSize;
    }
}
