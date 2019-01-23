package ng.max.binger.activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.max.binger.R;
import ng.max.binger.adapters.ViewPagerAdapter;
import ng.max.binger.fragments.AiringTodayFragment;
import ng.max.binger.fragments.FavoritesFragment;
import ng.max.binger.fragments.PopularShowFragment;
import ng.max.binger.receivers.DailyReceiver;
import ng.max.binger.services.SyncService;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private MenuItem prevMenuItem;
    ViewPagerAdapter viewPagerAdapter;

    PendingIntent myPendingIntent;
    AlarmManager alarmManager;
    Calendar firingCal;
    Intent intent;

    int DEFAULT_INT_VALUE = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today on Binger");

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewPagerSetup();

        // Get the extras (if there are any)
        intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("position")) {
                viewPager.setCurrentItem(getIntent().getIntExtra("position", DEFAULT_INT_VALUE));
            }
        }
        else {
            syncFavoriteTvShowsService();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Handle navigation view item clicks here.
        switch (menuItem.getItemId()) {
            case R.id.navigation_today:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_popular:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_favorite:
                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }


    public void viewPagerSetup() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AiringTodayFragment());
        viewPagerAdapter.addFragment(new PopularShowFragment());
        viewPagerAdapter.addFragment(new FavoritesFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void syncFavoriteTvShowsService() {
        firingCal = Calendar.getInstance();
        firingCal.set(Calendar.HOUR, 8);
        firingCal.set(Calendar.MINUTE, 0);
        firingCal.set(Calendar.SECOND, 0);
        long intendedTime = firingCal.getTimeInMillis();


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(this, DailyReceiver.class);
        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        //setting the repeating alarm that will be fired every day
        alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, myPendingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_cover_letter:
                // custom dialog
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView title = dialog.findViewById(R.id.title);
                TextView text = dialog.findViewById(R.id.text);
                ImageView image = dialog.findViewById(R.id.image);

                title.setText(R.string.cover_letter_title);
                text.setText(R.string.cover_letter_content);
                image.setImageResource(R.drawable.max);

                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
