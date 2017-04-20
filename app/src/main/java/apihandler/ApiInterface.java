package apihandler;

import model.AnniversaryModelResponse;
import model.EventResponse;
import model.ExhibitorResponse;
import model.GateResponsModel;
import model.HomeMenuModelResponse;
import model.MapResponse;
import model.TopicweekResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Alok on 26-09-2016.
 */
public interface ApiInterface {

    //get all catagory
    @POST("webservices/topicweek.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicWeek(@Field("lang") String lang);

    @POST("webservices/home-menu-web.php?")
    @FormUrlEncoded
    Call<HomeMenuModelResponse> getMenu(@Field("lang") String lang);

    @POST("webservices/audioguide.php?")
    @FormUrlEncoded
    Call<String> getAudio(@Field("lang") String lang);



    @POST("webservices/ann-partner.php?")
    @FormUrlEncoded
    Call<AnniversaryModelResponse> getAnniversaryPartner(@Field("lang") String lang);

    @POST("webservices/event_next.php?")
    @FormUrlEncoded
    Call<EventResponse> get7EventList(@Field("lang") String lang);


    @POST("webservices/event_gate_area.php?")
    @FormUrlEncoded
    Call<GateResponsModel> getGateList(@Field("lang") String lang);

    @POST("webservices/today_event_list.php?")
    @FormUrlEncoded
    Call<EventResponse> getTodayEventList(@Field("lang") String lang);

    @POST("webservices/next_day_event_list.php?")
    @FormUrlEncoded
    Call<EventResponse> getTomorrowEventList(@Field("lang") String lang);

    @POST("webservices/topic_month.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicInMonthWise(@Field("lang") String lang);


    @POST("webservices/date_wise_topic_week.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicInDateWise(@Field("lang") String lang);


    @POST("webservices/exhibitors_details.php?")
    @FormUrlEncoded
    Call<ExhibitorResponse> getExhibitorList(@Field("lang") String lang,
                                             @Field("date") String date,
                                             @Field("time") String time,
                                             @Field("tags") String tags);


    @POST("webservices/map_location_details.php?")
    @FormUrlEncoded
    Call<MapResponse> getMapDetail(@Field("lang") String lang);









}
