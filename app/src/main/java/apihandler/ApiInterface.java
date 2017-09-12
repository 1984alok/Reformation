package apihandler;

import com.google.gson.JsonObject;

import model.AnniversaryModelResponse;
import model.AudioResponse;
import model.EvenTCatg;
import model.EventResponse;
import model.EventdetailResponse;
import model.ExhibitorDetailResponseById;
import model.ExhibitorResponse;
import model.FaqResponse;
import model.GateDetailResponse;
import model.GateResponsModel;
import model.HomeMenuModelResponse;
import model.MapResponse;
import model.EventPlaceCatg;
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
    @POST("webservices_mob_app/topicweek.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicWeek(@Field("lang") String lang);

    @POST("webservices_mob_app/home-menu-web.php?")
    @FormUrlEncoded
    Call<HomeMenuModelResponse> getMenu(@Field("lang") String lang);

    @POST("webservices_mob_app/audioguide.php?")
    @FormUrlEncoded
    Call<AudioResponse> getAudio(@Field("lang") String lang);

    @POST("webservices_mob_app/faq.php?")
    @FormUrlEncoded
    Call<FaqResponse> getFaq(@Field("lang") String lang);



    @POST("webservices_mob_app/ann-partner.php?")
    @FormUrlEncoded
    Call<AnniversaryModelResponse> getAnniversaryPartner(@Field("lang") String lang);

    @POST("webservices_mob_app/event_next.php?")
    @FormUrlEncoded
    Call<EventResponse> get7EventList(@Field("lang") String lang);


    @POST("webservices_mob_app/event_gate_area.php?")
    @FormUrlEncoded
    Call<GateResponsModel> getGateList(@Field("lang") String lang);

    @POST("webservices_mob_app/today_event_list.php?")
    @FormUrlEncoded
    Call<EventResponse> getTodayEventList(@Field("lang") String lang);

    @POST("webservices_mob_app/next_day_event_list.php?")
    @FormUrlEncoded
    Call<EventResponse> getTomorrowEventList(@Field("lang") String lang);

    @POST("webservices_mob_app/topic_month.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicInMonthWise(@Field("lang") String lang);


    @POST("webservices_mob_app/date_wise_topic_week.php?")
    @FormUrlEncoded
    Call<TopicweekResponse> getTopicInDateWise(@Field("lang") String lang);


    @POST("webservices_mob_app/exhibitors_details.php?")
    @FormUrlEncoded
    Call<ExhibitorResponse> getExhibitorList(@Field("lang") String lang,
                                             @Field("date") String date,
                                             @Field("time") String time,
                                             @Field("tags") String tags);


    @POST("webservices_mob_app/map_location_details.php")
    @FormUrlEncoded
    Call<MapResponse> getMapDetail(@Field("lang") String lang);



    @POST("webservices_mob_app/event_details_via_id.php")
    @FormUrlEncoded
    Call<EventdetailResponse> getEventDetailById(@Field("lang") String lang,
                                                 @Field("event_id") String event_id );


    @POST("webservices_mob_app/gate_details_via_id.php")
    @FormUrlEncoded
    Call<GateDetailResponse> getGateDetaillById(@Field("lang") String lang,
                                                @Field("gatearea_id") String gatearea_id );



    @POST("webservices_mob_app/event_details_via_id.php")
    @FormUrlEncoded
    Call<JsonObject> chekResponse(@Field("lang") String lang,
                                  @Field("event_id") String event_id );

    @POST("webservices_mob_app/place_details_via_id.php")
    @FormUrlEncoded
    Call<ExhibitorDetailResponseById> getPlaceDetailById(@Field("lang") String lang,
                                                         @Field("place_id") String gatearea_id );


    //event Catg
    @POST("webservices_mob_app/get_category_details.php")
    @FormUrlEncoded
    Call<EvenTCatg> getEventCatg(@Field("lang") String lang
    );


    //event placeCatg
    @POST("webservices_mob_app/place_category_list.php")
    @FormUrlEncoded
    Call<EventPlaceCatg> getPlaceCatg(@Field("lang") String lang
    );


    //event search
    @POST("webservices_mob_app/search_topic_week.php")
    @FormUrlEncoded
    Call<EventResponse> searchTopicweek( @Field("lang") String lang,
                                         @Field("search") String searchTxt
    );

    //event filter
    @POST("webservices_mob_app/event_cat_date_search.php")
    @FormUrlEncoded
    Call<EventResponse> filterTopicweek(@Field("lang") String lang,
                                        @Field("date") String date,
                                        @Field("search") String searchTxt
    );



    @POST("webservices_mob_app/exhibitors_details_gatearea_id.php?")
    @FormUrlEncoded
    Call<ExhibitorResponse> getExhibitorListGateRelated(@Field("lang") String lang,
                                                        @Field("date") String date,
                                                        @Field("time") String time,
                                                        @Field("tags") String tags,
                                                        @Field("gateareaid") String gateareaid);




}
