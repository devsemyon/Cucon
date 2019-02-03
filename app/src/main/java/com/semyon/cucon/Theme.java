package com.semyon.cucon;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Objects;

class Theme {

    static int getTheme(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String prefTheme = settings.getString("theme", "light"); // the second parameter will be fallback if the preference is not found
        int theme = R.style.AppThemeLight;
        switch (Objects.requireNonNull(prefTheme)) {
            case "dark":
                theme = R.style.AppThemeDark;
            break;
        }
        return theme;
    }

    static void setTheme(String name, Context context){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        switch (name.toLowerCase()){
            case "dark":
            case "тёмная":
                name = "dark";
                break;
            case "light":
            case "светлая":
                name = "light";
                break;
        }
        editor.putString("theme", name); // the second parameter will be fallback if the preference is not found
        editor.apply();
    }
}
