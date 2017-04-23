package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 23-04-2017.
 */
public class GateDetailResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private ResponseData responseData;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public class ResponseData {
        @SerializedName("event_details")
        @Expose
        private ArrayList<EventModel> eventDetails = null;
        @SerializedName("gate_data")
        @Expose
        private EventDetailGateData gateData;

        public ArrayList<EventModel> getEventDetails() {
            return eventDetails;
        }

        public void setEventDetails(ArrayList<EventModel> eventDetails) {
            this.eventDetails = eventDetails;
        }

        public EventDetailGateData getGateData() {
            return gateData;
        }

        public void setGateData(EventDetailGateData gateData) {
            this.gateData = gateData;
        }
    }
}
