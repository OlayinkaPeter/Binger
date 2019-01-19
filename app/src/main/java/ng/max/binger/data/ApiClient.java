package ng.max.binger.data;

import ng.max.binger.utils.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
