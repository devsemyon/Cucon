package com.semyon.cucon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean darkTheme = sharedPref.getBoolean("dark_theme", false); // the second parameter will be fallback if the preference is not found
        if (darkTheme){
            setTheme(android.R.style.ThemeOverlay_Material_Dark);
        } else {
            setTheme(android.R.style.ThemeOverlay_Material_Light);
        }

        setContentView(R.layout.activity_settings);

        theme = findViewById(R.id.theme); // preference Key
        if (darkTheme){
            theme.setChecked(true);
        }

        theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = getBaseContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (isChecked) {
                    editor.putBoolean("dark_theme", true);
                } else {
                    editor.putBoolean("dark_theme", false);
                }
                editor.apply();
                final Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

                // postDelayed нужен для того чтобы switch работал плавно
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after
                        startActivity(intent);
                    }
                }, 160);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }
}