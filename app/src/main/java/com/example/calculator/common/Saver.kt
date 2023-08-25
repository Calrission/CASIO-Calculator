package com.example.calculator.common

import android.content.Context
import com.example.calculator.models.State

class Saver(context: Context) {
    private val sh = context.getSharedPreferences("0", Context.MODE_PRIVATE)

    fun saveState(
        state: State
    ){
        state.apply {
            sh.edit()
                .clear()
                .putString("main_text", mainText)
                .putString("second_text", secondText)
                .putBoolean("is_dark_theme", isDarkTheme)
                .putInt("id_operation_button", idOperationButton)
                .apply()
        }
    }

    fun loadState(): State {
        return State(
            mainText = sh.getString("main_text", "")!!,
            secondText = sh.getString("second_text", "")!!,
            isDarkTheme = sh.getBoolean("is_dark_theme", false),
            idOperationButton = sh.getInt("id_operation_button", 0)
        )
    }
}
