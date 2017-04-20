package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 21-04-2017.
 */
public class MapModel {
    @SerializedName("gates")
    @Expose
    private ArrayList<GateModel> gates = null;
    @SerializedName("places")
    @Expose
    private ArrayList<Exhibitor> places = null;

    public ArrayList<GateModel> getGates() {
        return gates;
    }

    public void setGates(ArrayList<GateModel> gates) {
        this.gates = gates;
    }

    public ArrayList<Exhibitor> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Exhibitor> places) {
        this.places = places;
    }
}
