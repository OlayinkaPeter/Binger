package ng.max.binger.data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("tv/airing_today")
    Observable<TvShowResponse> getTVAiringToday(@Query("api_key") String apiKey);

    @GET("tv/popular")
    Observable<TvShowResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("tv/{id}")
    Observable<TvShow> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}