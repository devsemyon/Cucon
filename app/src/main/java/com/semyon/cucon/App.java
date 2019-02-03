package com.semyon.cucon;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String fontFamily = Font.getFont(getBaseContext());
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/"+ fontFamily)
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }
}