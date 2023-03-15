package com.example.myapplication.other

import androidx.appcompat.app.AppCompatDelegate

object UiUtils {

    fun toggleNightMode() =
        AppCompatDelegate.setDefaultNightMode(
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
}