package com.example.calculator.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.calculator.controllers.MainActivityController
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.common.getColorAttr
import com.example.calculator.common.isDarkTheme
import com.example.calculator.models.State
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isDarkMode: Boolean = false
    private var nowOperationButton: TextView? = null
        set(value) {
            if (field != null){
                changeSelectOperationButton(nowOperationButton!!, false)
            }
            field = value
            field?.setBackgroundColor(getColorAttr(R.attr.colorAccentButton))
        }

    private val controller by lazy { MainActivityController(this) }

    private val numButtons by lazy {
        arrayListOf(
            binding.num0, binding.num1, binding.num2,
            binding.num3, binding.num4, binding.num5,
            binding.num6, binding.num7, binding.num8,
            binding.num9
        )
    }

    private val operationButtons by lazy {
        arrayListOf(
            binding.deleny, binding.minus,
            binding.plus, binding.multy,
            binding.mod
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkMode = isDarkTheme()

        numButtons.forEach{ btn ->
            btn.setOnClickListener{
                controller.tapDigit(btn.text.toString().toInt())
            }
        }
        
        operationButtons.forEach{ btn ->
            btn.setOnClickListener{
                controller.tapOperation(btn.text.toString())
                nowOperationButton = btn
            }
        }

        binding.c.setOnClickListener {
            controller.tapClear()
        }

        binding.cardSo.setOnClickListener {
            controller.calc()
        }

        binding.sunPanel.setOnClickListener {
            controller.switchThemeMode()
        }

        binding.deleteNum.setOnClickListener {
            controller.tapRemove()
        }

        binding.dot.setOnClickListener {
            controller.tapDot()
        }

        controller.initState()
    }

    fun showResultCalc(result: String){
        binding.secondText.text = binding.mainText.text.toString()
        binding.mainText.text = result
        unselectOperationButton()
    }

    fun clear(){
        binding.mainText.text = ""
        binding.secondText.text = ""
        unselectOperationButton()
    }

    fun setMainText(text: String){
        binding.mainText.text = text
    }

    fun getState(): State{
        return State(
            mainText = binding.mainText.text.toString(),
            secondText = binding.secondText.text.toString(),
            isDarkTheme = isDarkMode,
            idOperationButton = nowOperationButton?.id ?: 0
        )
    }

    fun setState(state: State){
        binding.apply {
            mainText.text = state.mainText
            secondText.text = state.secondText
            if (state.idOperationButton != 0) {
                val textView = findViewById<TextView>(state.idOperationButton)
                changeSelectOperationButton(textView, true)
                nowOperationButton = textView
            }
            if (isDarkMode != state.isDarkTheme) {
                isDarkMode = state.isDarkTheme
                controller.setThemeMode(state.isDarkTheme)
            }
        }
    }

    fun showError(error: String){
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun changeSelectOperationButton(button: TextView, isSelect: Boolean){
        button.setBackgroundColor(getColorAttr(if (isSelect) R.attr.colorAccentButton else R.attr.colorBackItem))
    }

    fun unselectOperationButton(){
        if (nowOperationButton != null) {
            changeSelectOperationButton(nowOperationButton!!, false)
            nowOperationButton = null
        }
    }

    override fun onStop() {
        controller.onStop()
        super.onStop()
    }
}
