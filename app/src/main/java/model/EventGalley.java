
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventGalley {

    @SerializedName("USER_ID")
    @Expose
    private String uSERID;
    @SerializedName("FILE_NAME")
    @Expose
    private String fILENAME;
    @SerializedName("FILE_SIZE")
    @Expose
    private String fILESIZE;
    @SerializedName("FILE_TYPE")
    @Expose
    private String fILETYPE;
    @SerializedName("DATE")
    @Expose
    private String dATE;
    @SerializedName("EVENT")
    @Expose
    private String eVENT;
    @SerializedName("USERNAME")
    @Expose
    private String uSERNAME;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;

    public String getUSERID() {
        return uSERID;
    }

    public void setUSERID(String uSERID) {
        this.uSERID = uSERID;
    }

    public String getFILENAME() {
        return fILENAME;
    }

    public void setFILENAME(String fILENAME) {
        this.fILENAME = fILENAME;
    }

    public String getFILESIZE() {
        return fILESIZE;
    }

    public void setFILESIZE(String fILESIZE) {
        this.fILESIZE = fILESIZE;
    }

    public String getFILETYPE() {
        return fILETYPE;
    }

    public void setFILETYPE(String fILETYPE) {
        this.fILETYPE = fILETYPE;
    }

    public String getDATE() {
        return dATE;
    }

    public void setDATE(String dATE) {
        this.dATE = dATE;
    }

    public String getEVENT() {
        return eVENT;
    }

    public void setEVENT(String eVENT) {
        this.eVENT = eVENT;
    }

    public String getUSERNAME() {
        return uSERNAME;
    }

    public void setUSERNAME(String uSERNAME) {
        this.uSERNAME = uSERNAME;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

}
