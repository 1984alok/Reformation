package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alok on 27-04-2017.
 */
public class FaqModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ques")
    @Expose
    private String ques;
    @SerializedName("ans")
    @Expose
    private String ans;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ques);
        dest.writeString(ans);
    }

    public static final Creator<FaqModel> CREATOR = new Creator<FaqModel>() {
        @Override
        public FaqModel createFromParcel(Parcel in) {
            return new FaqModel(in);
        }

        @Override
        public FaqModel[] newArray(int size) {
            return new FaqModel[size];
        }
    };


    public FaqModel(Parcel in) {
        ques = in.readString();
        ans = in.readString();
    }

}
