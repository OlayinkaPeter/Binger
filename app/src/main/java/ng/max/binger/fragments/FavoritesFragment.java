package ng.max.binger.fragments;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ng.max.binger.R;
import ng.max.binger.activities.DetailsActivity;
import ng.max.binger.activities.MainActivity;
import ng.max.binger.adapters.TvShowAdapter;
import ng.max.binger.data.AppDatabase;
import ng.max.binger.data.TvShow;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements TvShowAdapter.OnMovieItemClicked {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DATABASE_NAME = "movies_db";
    private AppDatabase appDatabase;

    private List<TvShow> tvShowList = new ArrayList<>();
    private TvShowAdapter mAdapter;
    ProgressBar progressBar;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        buildRoomDatabase();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.favoriteShows);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new TvShowAdapter(tvShowList, R.layout.list_item_tv_show, getActivity());
        mAdapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getAllFavouriteShows();

        return view;
    }

    private void buildRoomDatabase() {
        appDatabase = Room.databaseBuilder(getContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    private void getAllFavouriteShows() {
        appDatabase.favoriteShowDao().getAllFavouriteShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TvShow>>() {
                    @Override
                    public void accept(List<TvShow> favoriteShows) throws Exception {
                        tvShowList.clear();
                        tvShowList.addAll(favoriteShows);
                        mAdapter.notifyDataSetChanged();
                        hideProgressBar();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.toString());
                    }
                });
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void favoriteButtonClicked(int position) {
        final TvShow tvShow = tvShowList.get(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.favoriteShowDao().remove(tvShow);
            }
        }).start();

        Toast.makeText(getContext(), tvShow.getName() + " removed from Favorites", Toast.LENGTH_LONG).show();
        tvShowList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void containerClicked(int position) {
        TvShow tvShow = tvShowList.get(position);

        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("MOVIE_NAME", tvShow.getName());
        intent.putExtra("MOVIE_ID", tvShow.getId());

        startActivity(intent);
    }

}
