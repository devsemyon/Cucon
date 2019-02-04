package com.semyon.cucon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        int theme = Theme.getTheme(getBaseContext());
        String value = "Светлая";
        Language.set(this);

        switch (theme) {
            case R.style.AppThemeDark:
                setTheme(R.style.Widget_AppCompat_Toolbar);
                value = "Тёмная";
                break;
            case R.style.AppThemeLight:
                setTheme(R.style.Widget_AppCompat_Toolbar);
                break;
            default:
                setTheme(R.style.Widget_AppCompat_Toolbar);
                break;
        }

        float size = Font.getFontSize(getBaseContext());
        byte index;

        if (size == 0.75f) {
            index = 0;
        } else if (size == 1.0f) {
            index = 1;
        } else if (size == 1.25f) {
            index = 2;
        } else {
            index = 3;
        }

        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        Font.applyFontSize(getResources().getConfiguration(), getBaseContext(), getResources());
        addPreferencesFromResource(R.xml.settings);

        Preference github = findPreference("github");
        ListPreference themePref = (ListPreference) findPreference("theme");
        ListPreference textSize = (ListPreference) findPreference("textSize");
        ListPreference textFont = (ListPreference) findPreference("textFont");
        ListPreference language = (ListPreference) findPreference("language");
        language.setValue(Language.getFull(this));
        textFont.setValue(Font.getFont(getBaseContext()));
        textSize.setValueIndex(index);
        themePref.setValue(value);

        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Language.update(newValue.toString(), getBaseContext());
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        textFont.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Font.setFont(getBaseContext(), newValue.toString());
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });

        github.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SemyonNovikov/cucon"));
                startActivity(browserIntent);
                return true;
            }
        });

        themePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Theme.setTheme(newValue.toString(), getBaseContext());
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });


        textSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String size = newValue.toString();
                float size_f = 1.0f;

                switch (size) {
                    case "Small":
                    case "Маленький":
                        size_f = 0.75f;
                        break;
                    case "Medium":
                    case "Средний":
                        size_f = 1.0f;
                        break;
                    case "Large":
                    case "Большой":
                        size_f = 1.25f;
                        break;
                    case "Very large":
                    case "Огромный":
                        size_f = 1.5f;
                        break;
                }

                Font.setFontSize(getBaseContext(), size_f);
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}