package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 22-04-2017.
 */
public class EventDetailGateData {
    @SerializedName("data")
    @Expose
    private GateModel data;
    @SerializedName("audio")
    @Expose
    private ArrayList<Audio> audio = null;
    @SerializedName("gallery")
    @Expose
    private ArrayList<Gallery> gallery = null;

    public GateModel getData() {
        return data;
    }

    public void setData(GateModel data) {
        this.data = data;
    }

    public ArrayList<Audio> getAudio() {
        return audio;
    }

    public void setAudio(ArrayList<Audio> audio) {
        this.audio = audio;
    }

    public ArrayList<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<Gallery> gallery) {
        this.gallery = gallery;
    }
}
