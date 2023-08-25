package com.example.calculator.common

import android.content.res.Configuration
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity

fun ContextThemeWrapper.getColorAttr(idColor: Int): Int{
    val value = TypedValue()
    theme.resolveAttribute(idColor, value, true)
    return value.data
}

fun AppCompatActivity.isDarkTheme(): Boolean =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
