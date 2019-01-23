package ng.max.binger.model

import io.reactivex.Observable
import ng.max.binger.data.TvShowResponse

interface AiringTodayInteractor {
    fun getTVAiringToday(apiKey: String): Observable<TvShowResponse>
}
