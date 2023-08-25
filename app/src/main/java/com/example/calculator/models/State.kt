package com.example.calculator.models

data class State (
    var mainText: String,
    var secondText: String,
    var isDarkTheme: Boolean,
    var idOperationButton: Int = 0
)