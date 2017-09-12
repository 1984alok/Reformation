package apihandler;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by muvi on 29/8/17.
 */

public interface AdminApi {
    @POST("status.txt")
    Call<JsonObject> chekAdmin();
}
