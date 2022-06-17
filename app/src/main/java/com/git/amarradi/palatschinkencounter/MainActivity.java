package com.git.amarradi.palatschinkencounter;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements WipeDataDialog.WipeDialogListener {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COUNTER = "text";
    public static final String NIGHT_MODE = "night_mode";


    private int counter = 0;
    private TextView textView;
    private boolean nightMode;
    private boolean safedNightMode;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.mipmap.logo_psc_round);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        Typeface typeface = getResources().getFont(R.font.opensans_regular);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button counterTextButton = findViewById(R.id.counter_text_button);
        textView = findViewById(R.id.textview);
        TextView textView_start = findViewById(R.id.tv_startpage);
        textView.setTypeface(typeface);
        textView_start.setTypeface(typeface);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        safedNightMode = sharedPreferences.getBoolean(NIGHT_MODE, false);

        if (safedNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            counterTextButton.setTextColor(Color.WHITE);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        load_data();
        updateViews();

        counterTextButton.setOnClickListener(v -> {
            counter++;
            textView.setText(format("%d", counter));
            save_data();
        });
        load_data();
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_setting:
                Intent intentSetting = new Intent(this, SettingActivity.class);
                startActivity(intentSetting);
                return true;
            case R.id.item_clean:
                openDialog();
                return true;
            case R.id.item_dn_switch:
                switchDayNightMode();
                return true;
            case R.id.item_recipe:
                Intent intentRecipe = new Intent(this, RecipeActivity.class);
                startActivity(intentRecipe);
                return true;
            case R.id.item_about:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void openDialog() {
        WipeDataDialog dialog = new WipeDataDialog();
        dialog.show(getSupportFragmentManager(), "open dialog");
    }

    public void save_data() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COUNTER, counter);
        editor.apply();
    }

    public void load_data() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int safedCounter = sharedPreferences.getInt(COUNTER, 0);
        counter = safedCounter;
    }

    @SuppressLint("DefaultLocale")
    public void updateViews() {
        textView.setText(format("%d", counter));
    }

    public void switchDayNightMode() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        safedNightMode = sharedPreferences.getBoolean(NIGHT_MODE, false);

        nightMode = safedNightMode;
        if (safedNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            nightMode = false;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            nightMode = true;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE, nightMode);
        editor.apply();
    }


    public void reset_counter() {
        counter = 0;
        updateViews();
        save_data();
        Toast.makeText(this, this.getString(R.string.reset), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYesClicked() {
        reset_counter();
    }
}