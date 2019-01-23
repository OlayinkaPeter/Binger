package ng.max.binger.viewmodel

import io.reactivex.Observable
import io.reactivex.Scheduler
import ng.max.binger.data.TvShowResponse
import ng.max.binger.model.AiringTodayInteractor

class AiringTodayViewModel(private val airingTodayInteractor: AiringTodayInteractor, private val scheduler: Scheduler) {

    fun getTVAiringToday(apiKey: String): Observable<TvShowResponse> {
        return airingTodayInteractor.getTVAiringToday(apiKey).observeOn(scheduler)
    }
}
