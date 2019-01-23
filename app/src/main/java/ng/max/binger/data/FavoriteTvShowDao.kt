package ng.max.binger.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import io.reactivex.Single

@Dao
interface FavoriteTvShowDao {
    @get:Query("SELECT * FROM favorite_shows")
    val allFavouriteShows: Single<List<TvShow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvShow: TvShow)

    @Delete
    fun remove(tvShow: TvShow)

    @Query("SELECT count(*) FROM favorite_shows where id LIKE :id")
    fun isFavouriteShow(id: Long): Int
}

