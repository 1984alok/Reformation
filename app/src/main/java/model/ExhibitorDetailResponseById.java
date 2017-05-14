package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by muvi on 12/5/17.
 */

public class ExhibitorDetailResponseById {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private PlaceDetailData responseData;

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

    public PlaceDetailData getResponseData() {
        return responseData;
    }

    public void setResponseData(PlaceDetailData responseData) {
        this.responseData = responseData;
    }
}
