package ng.max.binger.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ng.max.binger.R;
import ng.max.binger.adapters.GenreAdapter;
import ng.max.binger.adapters.ProductionCompanyAdapter;
import ng.max.binger.data.ApiClient;
import ng.max.binger.data.ApiService;
import ng.max.binger.data.Genre;
import ng.max.binger.data.TvShow;
import ng.max.binger.data.ProductionCompany;
import ng.max.binger.utils.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private final static String API_KEY = Constants.API_KEY;

    //Binding the views via the Butter Knife library
    private View rootView;
    @BindView(R.id.detailLayout)
    LinearLayout mDetailLayout;
    @BindView(R.id.moviePoster)
    ImageView mMoviePoster;
    @BindView(R.id.movieTitle)
    TextView mMovieTitle;
    @BindView(R.id.movieYear)
    TextView mMovieYear;
    @BindView(R.id.movieSummary)
    TextView mMovieSummary;
    @BindView(R.id.showStatus)
    TextView mShowStatus;

    String movieTitleString;
    int movieId;

    private List<Genre> genreList = new ArrayList<>();
    private GenreAdapter genreAdapter;

    private List<ProductionCompany> productionCompanyList = new ArrayList<>();
    private ProductionCompanyAdapter productionCompanyAdapter;

    RecyclerView genreRecyclerView, productionCompanyRecyclerView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        mMoviePoster = findViewById(R.id.moviePoster);
        mMovieTitle = findViewById(R.id.movieTitle);
        mMovieYear = findViewById(R.id.movieYear);
        mMovieSummary = findViewById(R.id.movieSummary);
        mShowStatus = findViewById(R.id.showStatus);

        genreRecyclerView = (RecyclerView) findViewById(R.id.genreRecyclerView);
        genreRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        productionCompanyRecyclerView = (RecyclerView) findViewById(R.id.productionCompanyRecyclerView);
        productionCompanyRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        genreAdapter = new GenreAdapter(genreList, R.layout.list_item_genre, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        genreRecyclerView.setLayoutManager(mLayoutManager);
        genreRecyclerView.setItemAnimator(new DefaultItemAnimator());
        genreRecyclerView.setAdapter(genreAdapter);

        productionCompanyAdapter = new ProductionCompanyAdapter(productionCompanyList, R.layout.list_item_production_company, getApplicationContext());
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        productionCompanyRecyclerView.setLayoutManager(pLayoutManager);
        productionCompanyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productionCompanyRecyclerView.setAdapter(productionCompanyAdapter);

        Intent intent = getIntent();
        movieTitleString = intent.getExtras().getString("MOVIE_NAME");
        movieId = intent.getExtras().getInt("MOVIE_ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(movieTitleString);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiService = ApiClient.getClient().create(ApiService.class);

       // getMovieDetails();
        getMovieDetails(movieId, API_KEY);
    }


    private void getMovieDetails(int id, String API_KEY) {
        disposable.add(
                apiService.getMovieDetails(id, API_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<TvShow>() {
                            @Override
                            public void accept(TvShow tvShow) throws Exception {
                                mMovieTitle.setText(tvShow.getName());
                                mMovieSummary.setText(tvShow.getOverview());
                                mMovieYear.setText(tvShow.getReleaseDate());
                                mShowStatus.setText(tvShow.getStatus());

                                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w500/" + tvShow.getBackdropPath()).into(mMoviePoster);

                                genreList.clear();
                                genreList.addAll(tvShow.getGenres());
                                genreAdapter.notifyDataSetChanged();

                                productionCompanyList.clear();
                                productionCompanyList.addAll(tvShow.getProductionCompanies());
                                productionCompanyAdapter.notifyDataSetChanged();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, throwable.toString());
                            }
                        })
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}