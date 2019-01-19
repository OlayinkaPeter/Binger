package ng.max.binger.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface FavoriteTvShowDao {
    @Query("SELECT * FROM favorite_shows")
    Single<List<TvShow>> getAllFavouriteShows();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TvShow tvShow);

    @Delete
    void remove(TvShow tvShow);

    @Query("SELECT count(*) FROM favorite_shows where id LIKE :id")
    int isFavouriteShow(long id);
}

