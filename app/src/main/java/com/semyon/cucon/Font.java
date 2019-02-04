package com.semyon.cucon;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

public class Font {

    static void setFont(Context context, String font) {
        SharedPreferences.Editor editor = context.getSharedPreferences("font", MODE_PRIVATE).edit();
        editor.putString("font", font);
        editor.apply(); // сохраняем изменения
    }

    public static String getFont(Context context){
        SharedPreferences prefs = context.getSharedPreferences("font", MODE_PRIVATE);
        return prefs.getString("font", "Lato");
    }

    static void applyFontSize(Configuration configuration, Context context, Resources resources) {
        configuration.fontScale = getFontSize(context);
        DisplayMetrics metrics = resources.getDisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        context.getResources().updateConfiguration(configuration, metrics);
    }

    static float getFontSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("font", MODE_PRIVATE);
        return prefs.getFloat("size", 1.0f);
    }

    static void setFontSize(Context context, float size) {
        SharedPreferences.Editor editor = context.getSharedPreferences("font", MODE_PRIVATE).edit();
        editor.putFloat("size", size);
        editor.apply(); // сохраняем изменения
    }
}