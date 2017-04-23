package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 22-04-2017.
 */
public class EventResponseData {

    @SerializedName("gate_data")
    @Expose
    private EventDetailGateData gateData;
    @SerializedName("place_data")
    @Expose
    private EventDetailPlaceData placeData;
    @SerializedName("event_details")
    @Expose
    private EventModel eventDetails;
    @SerializedName("event_galley")
    @Expose
    private ArrayList<Gallery> eventGalley = null;

    public EventDetailGateData getGateData() {
        return gateData;
    }

    public void setGateData(EventDetailGateData gateData) {
        this.gateData = gateData;
    }

    public EventDetailPlaceData getPlaceData() {
        return placeData;
    }

    public void setPlaceData(EventDetailPlaceData placeData) {
        this.placeData = placeData;
    }

    public EventModel getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventModel eventDetails) {
        this.eventDetails = eventDetails;
    }

    public ArrayList<Gallery> getEventGalley() {
        return eventGalley;
    }

    public void setEventGalley(ArrayList<Gallery> eventGalley) {
        this.eventGalley = eventGalley;
    }

}
