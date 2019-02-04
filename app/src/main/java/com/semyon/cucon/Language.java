package com.semyon.cucon;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.os.ConfigurationCompat;
import java.util.Locale;

public class Language {

    private static final String APP_LANGUAGE = "language";
    private static SharedPreferences language_pref;

    // функция установки языка перед setContentView()
    public static void set(Context context) {
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale(get(context));
        Locale.setDefault(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    // функция для записи в sharedPref нового языка
    static void update(String language, Context context) {
        if (language.toLowerCase().contains("ru") || language.toLowerCase().contains("ру")){
            language = "ru";
        } else {
            language = "en";
        }
        language_pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = language_pref.edit();
        edit.putString(APP_LANGUAGE, language);
        edit.apply();
        set(context);
    }

    // получение текущего языка из sharedPref
    private static String get(Context context){
        language_pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (language_pref.contains(APP_LANGUAGE)){
            return language_pref.getString(APP_LANGUAGE, "ru");
        } else {
            String s = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).toString();
            update(s, context);
            return s;
        }
    }

    // получения текущего языка в полном формате
    static String getFull(Context context){
        String language = get(context);
        if (language.equalsIgnoreCase("ru") || language.equalsIgnoreCase("русский") || language.contains("ru")){
            language = "Русский";
        }else {
            language = "English";
        }
        return language;
    }
}
