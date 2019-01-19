package ng.max.binger.fragments;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ng.max.binger.R;
import ng.max.binger.activities.DetailsActivity;
import ng.max.binger.activities.MainActivity;
import ng.max.binger.adapters.TvShowAdapter;
import ng.max.binger.data.ApiClient;
import ng.max.binger.data.ApiService;
import ng.max.binger.data.AppDatabase;
import ng.max.binger.data.TvShow;
import ng.max.binger.data.TvShowResponse;
import ng.max.binger.utils.Constants;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularShowFragment extends Fragment implements TvShowAdapter.OnMovieItemClicked {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();

    private static final String DATABASE_NAME = "movies_db";
    private AppDatabase appDatabase;

    private final static String API_KEY = Constants.API_KEY;
    private List<TvShow> tvShowList = new ArrayList<>();
    private TvShowAdapter mAdapter;
    ProgressBar progressBar;

    public PopularShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_shows, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getActivity(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
        }

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.popularTvShows);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        apiService =
                ApiClient.getClient().create(ApiService.class);

        mAdapter = new TvShowAdapter(tvShowList, R.layout.list_item_tv_show, getActivity());
        mAdapter.setListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getPopularMovies(API_KEY);
        buildRoomDatabase();

        return view;
    }

    private void getPopularMovies(String API_KEY) {
        disposable.add(
                apiService.getPopularMovies(API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<TvShowResponse>() {
                            @Override
                            public void accept(TvShowResponse tvShowResponse) throws Exception {
                                tvShowList.clear();
                                tvShowList.addAll(tvShowResponse.getResults());
                                mAdapter.notifyDataSetChanged();
                                hideProgressBar();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                showError(throwable);
                                hideProgressBar();
                            }
                        })
        );
    }

    private void showError(Throwable e) {
        String message = "";
        try {
            if (e instanceof IOException) {
                message = "No internet connection!";
            } else if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (TextUtils.isEmpty(message)) {
            message = "Unknown error occurred! Check LogCat.";
        }

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void buildRoomDatabase() {
        appDatabase = Room.databaseBuilder(getContext(),
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
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
                appDatabase.favoriteShowDao().insert(tvShow);
            }
        }).start();

        Toast.makeText(getContext(), tvShow.getName() + " added to Favorites", Toast.LENGTH_LONG).show();
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
