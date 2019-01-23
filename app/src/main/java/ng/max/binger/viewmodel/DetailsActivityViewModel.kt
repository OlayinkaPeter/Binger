package ng.max.binger.viewmodel

import io.reactivex.Observable
import io.reactivex.Scheduler
import ng.max.binger.data.TvShow
import ng.max.binger.model.DetailsActivityInteractor

class DetailsActivityViewModel(private val detailsActivityInteractor: DetailsActivityInteractor, private val scheduler: Scheduler) {

    fun getMovieDetails(id: Int, apiKey: String): Observable<TvShow> {
        return detailsActivityInteractor.getMovieDetails(id, apiKey).observeOn(scheduler)
    }
}
