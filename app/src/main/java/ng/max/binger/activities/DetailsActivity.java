package ng.max.binger.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import ng.max.binger.R;
import ng.max.binger.adapters.GenreAdapter;
import ng.max.binger.adapters.ProductionCompanyAdapter;
import ng.max.binger.data.ApiService;
import ng.max.binger.data.Genre;
import ng.max.binger.data.TvShow;
import ng.max.binger.data.ProductionCompany;
import ng.max.binger.model.DetailsActivityInteractorImpl;
import ng.max.binger.utils.Constants;
import ng.max.binger.viewmodel.DetailsActivityViewModel;
import retrofit2.HttpException;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();
    private final static String API_KEY = Constants.API_KEY;

    private DetailsActivityViewModel detailsActivityViewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.genreRecyclerView)
    RecyclerView genreRecyclerView;
    @BindView(R.id.productionCompanyRecyclerView)
    RecyclerView productionCompanyRecyclerView;


    String movieTitleString;
    int movieId;

    private List<Genre> genreList = new ArrayList<>();
    private GenreAdapter genreAdapter;

    private List<ProductionCompany> productionCompanyList = new ArrayList<>();
    private ProductionCompanyAdapter productionCompanyAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        detailsActivityViewModel = new DetailsActivityViewModel(new DetailsActivityInteractorImpl(), AndroidSchedulers.mainThread());

        genreRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(movieTitleString);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getMovieDetails(movieId, API_KEY);
    }


    private void getMovieDetails(int id, String API_KEY) {
        disposable.add(
                detailsActivityViewModel.getMovieDetails(id, API_KEY)
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
                                showError(throwable);
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

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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