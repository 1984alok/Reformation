package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alok on 26-03-2017.
 */
public class TopicweekResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("responseData")
    @Expose
    private ArrayList<TopicWeekModel> responseData = null;

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

    public ArrayList<TopicWeekModel> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<TopicWeekModel> responseData) {
        this.responseData = responseData;
    }


    public class TopicWeekModel {

        @SerializedName("header_pic")
        @Expose
        private String headerPic;
        @SerializedName("to_week_title")
        @Expose
        private String toWeekTitle;
        @SerializedName("to_week_des")
        @Expose
        private String toWeekDes;
        @SerializedName("per_start")
        @Expose
        private String perStart;
        @SerializedName("per_end")
        @Expose
        private String perEnd;
        @SerializedName("event")
        @Expose
        private ArrayList<Event> event = null;

        public String getHeaderPic() {
            return headerPic;
        }

        public void setHeaderPic(String headerPic) {
            this.headerPic = headerPic;
        }

        public String getToWeekTitle() {
            return toWeekTitle;
        }

        public void setToWeekTitle(String toWeekTitle) {
            this.toWeekTitle = toWeekTitle;
        }

        public String getToWeekDes() {
            return toWeekDes;
        }

        public void setToWeekDes(String toWeekDes) {
            this.toWeekDes = toWeekDes;
        }

        public String getPerStart() {
            return perStart;
        }

        public void setPerStart(String perStart) {
            this.perStart = perStart;
        }

        public String getPerEnd() {
            return perEnd;
        }

        public void setPerEnd(String perEnd) {
            this.perEnd = perEnd;
        }

        public ArrayList<Event> getEvent() {
            return event;
        }

        public void setEvent(ArrayList<Event> event) {
            this.event = event;
        }

    }
    public class Event {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("titel")
        @Expose
        private String titel;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("start")
        @Expose
        private String start;
        @SerializedName("date")
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitel() {
            return titel;
        }

        public void setTitel(String titel) {
            this.titel = titel;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }


}
