package com.example.calculator

import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.models.State
import net.objecthunter.exp4j.ExpressionBuilder

class Controller(private val activity: MainActivity) {

    private val saver: Saver = Saver(activity)
    private val state: State
        get() {
            return activity.getState()
        }

    fun initState(){
        activity.setState(saver.loadState())
    }

    fun calc(){
        try {
            val match = if (state.mainText.isNotEmpty()) {
                ExpressionBuilder(state.mainText).build()
            }else {
                ExpressionBuilder(state.secondText).build()
            }
            val result = match!!.evaluate()
            activity.showResultCalc(result)
        }catch (e: Exception){
            activity.showError("Ошибка!\n${e.message}")
        }
    }

    fun switchThemeMode(){
        val nowIsDarkTheme = activity.isDarkTheme()
        setThemeMode(!nowIsDarkTheme)
    }

    fun setThemeMode(isDarkTheme: Boolean){
        if (isDarkTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val newState = state
        newState.isDarkTheme = isDarkTheme
        saver.saveState(newState)
    }
}