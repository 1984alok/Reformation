package apihandler;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alok on 26-09-2016.
 */
public class ApiClient {

    // Test QA
    // public static final String BASE_URL = "http://www.inoasoft.info/projects/eventmanagement/";

    //Production
    public static final String BASE_URL = "http://www.inoasoft.info/projects/event2017/";


    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
