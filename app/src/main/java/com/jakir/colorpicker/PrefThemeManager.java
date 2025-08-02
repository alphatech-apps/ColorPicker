package com.jakir.colorpicker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

//
// Created by JAKIR HOSSAIN on 7/30/2025.
//
    public class PrefThemeManager {
        private static final String PREFS_NAME = "theme_prefs";
        private static final String KEY_ACCENT = "selected_accent";

        public static void saveAccentColor(Context context, int color) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putInt(KEY_ACCENT, color).apply();
        }

        public static int getAccentColor(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return prefs.getInt(KEY_ACCENT, ContextCompat.getColor(context, R.color.selected_accent));
        }
    }
