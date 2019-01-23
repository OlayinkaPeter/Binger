package ng.max.binger.viewmodel

import io.reactivex.Observable
import io.reactivex.Scheduler
import ng.max.binger.data.TvShowResponse
import ng.max.binger.model.PopularShowInteractor

class PopularShowViewModel(private val popularShowInteractor: PopularShowInteractor, private val scheduler: Scheduler) {

    fun getPopularMovies(apiKey: String): Observable<TvShowResponse> {
        return popularShowInteractor.getPopularMovies(apiKey).observeOn(scheduler)
    }
}
