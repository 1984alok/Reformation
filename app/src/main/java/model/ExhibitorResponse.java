package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by IMFCORP\alok.acharya on 17/4/17.
 */

public class ExhibitorResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private ArrayList<Exhibitor> responseData = null;

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

    public ArrayList<Exhibitor> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<Exhibitor> responseData) {
        this.responseData = responseData;
    }
}
