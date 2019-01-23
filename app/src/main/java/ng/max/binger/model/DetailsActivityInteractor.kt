package ng.max.binger.model

import io.reactivex.Observable
import ng.max.binger.data.TvShow
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsActivityInteractor {
    fun getMovieDetails(id: Int, apiKey: String): Observable<TvShow>
}