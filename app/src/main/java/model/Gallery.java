package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alok on 10-04-2017.
 */
public class Gallery {
    @SerializedName("USER_ID")
    @Expose
    private String imgId;
    @SerializedName("gate_id")
    @Expose
    private String gateId;
    @SerializedName("FILE_NAME")
    @Expose
    private String fileName;
    @SerializedName("FILE_SIZE")
    @Expose
    private String fileSize;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
