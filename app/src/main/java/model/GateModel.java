package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 10-04-2017.
 */
public class GateModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gate_unique_id")
    @Expose
    private String gateUniqueId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("title_long")
    @Expose
    private String titleLong;
    @SerializedName("descp")
    @Expose
    private String description;
    @SerializedName("pimage")
    @Expose
    private String headerImage;
    @SerializedName("gallary")
    @Expose
    private ArrayList<Gallery> gallary = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGateUniqueId() {
        return gateUniqueId;
    }

    public void setGateUniqueId(String gateUniqueId) {
        this.gateUniqueId = gateUniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLong() {
        return titleLong;
    }

    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public ArrayList<Gallery> getGallary() {
        return gallary;
    }

    public void setGallary(ArrayList<Gallery> gallary) {
        this.gallary = gallary;
    }
}
