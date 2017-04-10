package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 09-04-2017.
 */
public class EventResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private ArrayList<TopicweekResponse.Event> responseData = null;

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

    public ArrayList<TopicweekResponse.Event> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<TopicweekResponse.Event> responseData) {
        this.responseData = responseData;
    }
}
