package apihandler;

import java.util.Map;

import model.AnniversaryModelResponse;
import model.EventResponse;
import model.GateResponsModel;
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
import retrofit2.http.Field;

/**
 * Created by Alok on 26-09-2016.
 */
public interface ApiInterface {

    //get all catagory
    @POST("topicweek.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicWeek(@Field("lang") String lang);

    @POST("home-menu-web.php?")
    @FormUrlEncoded
    Call<HomeMenuModelResponse> getMenu(@Field("lang") String lang);

    @POST("audioguide.php?")
    @FormUrlEncoded
    Call<String> getAudio(@Field("lang") String lang);



    @POST("ann-partner.php?")
    @FormUrlEncoded
    Call<AnniversaryModelResponse> getAnniversaryPartner(@Field("lang") String lang);

    @POST("event_next.php?")
    @FormUrlEncoded
    Call<EventResponse> get7EventList(@Field("lang") String lang);


    @POST("event_gate_area.php?")
    @FormUrlEncoded
    Call<GateResponsModel> getGateList(@Field("lang") String lang);

    @POST("today_event_list.php?")
    @FormUrlEncoded
    Call<EventResponse> getTodayEventList(@Field("lang") String lang);

    @POST("next_day_event_list.php?")
    @FormUrlEncoded
    Call<EventResponse> getTomorrowEventList(@Field("lang") String lang);

    @POST("topic_month.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicInMonthWise(@Field("lang") String lang);


    @POST("date_wise_topic_week.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicInDateWise(@Field("lang") String lang);







}
