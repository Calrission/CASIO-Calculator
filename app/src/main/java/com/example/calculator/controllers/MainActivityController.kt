package com.example.calculator.controllers

import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.common.Expression
import com.example.calculator.common.Operations
import com.example.calculator.common.Saver
import com.example.calculator.common.isBeautiful
import com.example.calculator.models.State
import com.example.calculator.screens.MainActivity

class MainActivityController(private val activity: MainActivity) {

    private val expression = Expression()

    private val saver: Saver = Saver(activity)
    private val state: State
        get() {
            return activity.getState()
        }

    fun initState(){
        val lastState = saver.loadState()
        expression.parse(lastState.mainText)
        activity.setState(lastState)
    }

    fun tapDigit(digit: Int){
        expression.addNewDigit(digit)
        refreshMainText()
    }

    fun tapOperation(operation: String){
        expression.setOperation(operation)
        refreshMainText()
    }

    fun tapRemove(){
        expression.remove()
        if (expression.operation == Operations.EMPTY){
            activity.unselectOperationButton()
        }
        refreshMainText()
    }

    fun tapClear(){
        expression.clear()
        activity.clear()
    }

    fun onStop(){
        saver.saveState(activity.getState())
    }

    fun calc(){
        try {
            if (state.mainText.isNotEmpty()) {
                val result = expression.calc().isBeautiful()
                activity.showResultCalc(result)
                expression.clear()
                expression.first = result
            }
        }catch (e: Exception){
            activity.showError("Ошибка!\n${e.message}")
        }
    }

    fun switchThemeMode(){
        val nowIsDarkTheme = activity.isDarkMode
        setThemeMode(!nowIsDarkTheme)
    }

    fun setThemeMode(isDarkTheme: Boolean){
        activity.isDarkMode = isDarkTheme
        if (isDarkTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun tapDot() {
        expression.addDot()
        refreshMainText()
    }
    
    private fun refreshMainText(){
        activity.setMainText(expression.toString())
    }
}
