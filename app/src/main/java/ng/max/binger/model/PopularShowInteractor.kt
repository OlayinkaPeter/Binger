package ng.max.binger.model

import io.reactivex.Observable
import ng.max.binger.data.TvShowResponse

interface PopularShowInteractor {
    fun getPopularMovies(apiKey: String): Observable<TvShowResponse>

}
