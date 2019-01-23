package ng.max.binger.data

import com.google.gson.annotations.SerializedName

class TvShowResponse {
    @SerializedName("page")
    var page: Int = 0
    @SerializedName("results")
    var results: List<TvShow>? = null
    @SerializedName("total_results")
    var totalResults: Int = 0
    @SerializedName("total_pages")
    var totalPages: Int = 0
}
