package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 26-03-2017.
 */
public class HomeMenuModelResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private ArrayList<MenuModel> responseData = null;

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

    public ArrayList<MenuModel> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<MenuModel> responseData) {
        this.responseData = responseData;
    }

    public class MenuModel {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("external-link")
        @Expose
        private String externalLink;
        @SerializedName("external-link-status")
        @Expose
        private String externalLinkStatus;
        @SerializedName("pos")
        @Expose
        private String pos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getExternalLinkStatus() {
            return externalLinkStatus;
        }

        public void setExternalLinkStatus(String externalLinkStatus) {
            this.externalLinkStatus = externalLinkStatus;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

    }
}
