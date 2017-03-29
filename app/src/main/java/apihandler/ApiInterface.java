package apihandler;

import java.util.Map;

import model.HomeMenuModelResponse;
import model.TopicweekResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Alok on 26-09-2016.
 */
public interface ApiInterface {

    //get all catagory
    @POST("topicweek.php?")
    Call<TopicweekResponse> getTopicWeek(@Query("lang") String lang);

    @POST("home-menu-web.php?")
    Call<HomeMenuModelResponse> getMenu(@Query("lang") String lang);

    @POST("audioguide.php?")
    Call<String> getAudio(@Query("lang") String lang);








}
