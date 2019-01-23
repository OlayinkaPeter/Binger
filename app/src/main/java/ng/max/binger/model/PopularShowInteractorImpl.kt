package ng.max.binger.model

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ng.max.binger.data.ApiService
import ng.max.binger.data.TvShowResponse
import ng.max.binger.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PopularShowInteractorImpl : PopularShowInteractor {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    override fun getPopularMovies(apiKey: String): Observable<TvShowResponse> {
        return apiService.getPopularMovies(apiKey).subscribeOn(Schedulers.io())
    }
}
