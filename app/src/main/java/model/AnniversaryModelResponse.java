package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 02-04-2017.
 */
public class AnniversaryModelResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private ArrayList<ResponseModel> responseData = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<ResponseModel> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<ResponseModel> responseData) {
        this.responseData = responseData;
    }


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("external-link")
    @Expose
    private String externalLink;
    @SerializedName("pos")
    @Expose
    private String pos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public class ResponseModel {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("external-link")
        @Expose
        private String externalLink;
        @SerializedName("pos")
        @Expose
        private String pos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getExternalLink() {
            return externalLink;
        }

        public void setExternalLink(String externalLink) {
            this.externalLink = externalLink;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

    }
}
