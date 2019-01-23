package ng.max.binger.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("tv/airing_today")
    fun getTVAiringToday(@Query("api_key") apiKey: String): Observable<TvShowResponse>

    @GET("tv/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Observable<TvShowResponse>

    @GET("tv/{id}")
    fun getMovieDetails(@Path("id") id: Int, @Query("api_key") apiKey: String): Observable<TvShow>
}