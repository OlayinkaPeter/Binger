package ng.max.binger.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
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
import ng.max.binger.activities.MainActivity;
import ng.max.binger.data.ApiService;
import ng.max.binger.data.AppDatabase;
import ng.max.binger.data.TvShow;
import ng.max.binger.model.DetailsActivityInteractorImpl;
import ng.max.binger.utils.Constants;
import ng.max.binger.viewmodel.DetailsActivityViewModel;
import retrofit2.HttpException;

public class SyncService extends IntentService {
    private int result = Activity.RESULT_CANCELED;

    private static final String DATABASE_NAME = "movies_db";
    private AppDatabase appDatabase;
    private CompositeDisposable disposable = new CompositeDisposable();
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<TvShow> tvShowList = new ArrayList<>();
    private final static String API_KEY = Constants.API_KEY;

    private DetailsActivityViewModel detailsActivityViewModel;

    public SyncService() {
        super("SyncService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {

        Toast.makeText(this, "Binger is updating your favorite Tv shows", Toast.LENGTH_LONG).show();

        try {
            appDatabase = Room.databaseBuilder(this,
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

            disposable.add(
                    appDatabase.favoriteShowDao().getAllFavouriteShows()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<TvShow>>() {
                                @Override
                                public void accept(List<TvShow> favoriteShows) throws Exception {
                                    tvShowList.clear();
                                    tvShowList.addAll(favoriteShows);

                                    updateFavoriteShows(tvShowList);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    showError(throwable);
                                }
                            })
            );

            // successfully finished
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFavoriteShows(List<TvShow> tvShowList) {
        detailsActivityViewModel = new DetailsActivityViewModel(new DetailsActivityInteractorImpl(), AndroidSchedulers.mainThread());

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        for (TvShow tvShow : tvShowList) {
            disposable.add(
                    detailsActivityViewModel.getMovieDetails(tvShow.getId(), API_KEY)
                            .subscribe(new Consumer<TvShow>() {
                                @Override
                                public void accept(final TvShow tvShow) throws Exception {

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            appDatabase.favoriteShowDao().insert(tvShow);
                                        }
                                    }).start();

                                    Toast.makeText(getApplicationContext(), tvShow.getName() + " added to Favorites", Toast.LENGTH_LONG).show();
                                    }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    showError(throwable);
                                }
                            })
            );
        }
        notifyUser(getApplicationContext(), "Favorite Tv Shows on Binger", "All your favorite Tv shows have been updated");
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
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void notifyUser(Context context, String title, String content) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("position", 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_favorite)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1000, mBuilder.build());
    }
}