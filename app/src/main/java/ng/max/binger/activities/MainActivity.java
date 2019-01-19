package ng.max.binger.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ng.max.binger.R;
import ng.max.binger.fragments.AiringTodayFragment;
import ng.max.binger.fragments.FavoritesFragment;
import ng.max.binger.fragments.PopularShowFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today on Binger");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new AiringTodayFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;

        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.navigation_today) {
            getSupportActionBar().setTitle("Today on Binger");
            fragment = new AiringTodayFragment();
            loadFragment(fragment);
        } else if (id == R.id.navigation_popular) {
            getSupportActionBar().setTitle("Popular on Binger");
            fragment = new PopularShowFragment();
            loadFragment(fragment);
        } else if (id == R.id.navigation_favorite) {
            getSupportActionBar().setTitle("Your favorites on Binger");
            fragment = new FavoritesFragment();
            loadFragment(fragment);
        }

        return true;
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
