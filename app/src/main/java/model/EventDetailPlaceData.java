package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alok on 22-04-2017.
 */
public class EventDetailPlaceData {
    @SerializedName("data")
    @Expose
    private Exhibitor data;
    @SerializedName("gate_data")
    @Expose
    private EventDetailGateData gateData;

    public Exhibitor getData() {
        return data;
    }

    public void setData(Exhibitor data) {
        this.data = data;
    }

    public EventDetailGateData getGateData() {
        return gateData;
    }

    public void setGateData(EventDetailGateData gateData) {
        this.gateData = gateData;
    }

}
