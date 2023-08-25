package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.models.State
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var nowOperationButton: TextView? = null
        set(value) {
            if (field == null && value != null){
                addNewChar(value.text.toString())
            }else if (field != null && value != null) {
                field!!.setBackgroundColor(getColorAttr(R.attr.colorBackItem))
                binding.mainText.text = replaceOperation(value.text.toString(), field!!.text.toString())
            }
            field = value
            value?.setBackgroundColor(getColorAttr(R.attr.colorAccentButton))
        }

    private val controller by lazy { Controller(this) }

    private val numButtons by lazy {
        arrayListOf(
            binding.num0, binding.num1, binding.num2,
            binding.num3, binding.num4, binding.num5,
            binding.num6, binding.num7, binding.num8,
            binding.num9, binding.procent, binding.dot
        )
    }

    private val operationButtons by lazy {
        arrayListOf(
            binding.deleny, binding.minus,
            binding.plus, binding.multy
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        numButtons.forEach{ btn ->
            btn.setOnClickListener{
                addNewChar(btn.text.toString())
            }
        }
        
        operationButtons.forEach{ btn ->
            btn.setOnClickListener{
                nowOperationButton = btn
            }
        }

        binding.c.setOnClickListener {
            clear()
        }

        binding.cardSo.setOnClickListener {
            controller.calc()
        }

        binding.sunPanel.setOnClickListener {
            controller.switchThemeMode()
        }

        binding.deleteNum.setOnClickListener {
            deleteChar()
        }

        controller.initState()
    }

    fun showResultCalc(result: Double){
        binding.secondText.text = binding.mainText.text.toString()
        if (result == 0.0 || result % result.toInt().toDouble() == 0.0 ){
            binding.mainText.text = result.toInt().toString()
        }else{
            binding.mainText.text = result.toString()
        }
        toNormalNowOperationButton()
    }

    private fun addNewChar(char: String){
        binding.mainText.append(char)
    }

    private fun deleteChar(){
        try {
            val deleteChar = binding.mainText.text.toString().last().toString()
            binding.mainText.text =
                binding.mainText.text.toString().substring(0, binding.mainText.text.toString().length - 1)
            if (deleteChar in arrayListOf("+", "-", "/", "*")) toNormalNowOperationButton()
        }catch (_: Exception){}
    }

    private fun replaceOperation(newOperation: String, oldOperation: String): String {
        return binding.mainText.text.toString().reversed().replaceFirst(oldOperation, newOperation).reversed()
    }

    private fun clear(){
        binding.mainText.text = ""
        binding.secondText.text = ""
        toNormalNowOperationButton()
    }

    fun getState(): State{
        return State(
            mainText = binding.mainText.text.toString(),
            secondText = binding.secondText.text.toString(),
            isDarkTheme = isDarkTheme(),
            idOperationButton = nowOperationButton?.id ?: 0
        )
    }

    fun setState(state: State){
        binding.apply {
            mainText.text = state.mainText
            secondText.text = state.secondText
            if (state.idOperationButton != 0) {
                val textView = findViewById<TextView>(state.idOperationButton)
                textView.setBackgroundColor(getColorAttr(R.attr.colorAccentButton))
                nowOperationButton = textView
            }
            val nowDark = isDarkTheme()
            if (nowDark != state.isDarkTheme)
                controller.setThemeMode(state.isDarkTheme)
        }
    }

    fun showError(error: String){
        Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun toNormalNowOperationButton(){
        if (nowOperationButton != null) {
            nowOperationButton!!.setBackgroundColor(getColorAttr(R.attr.colorBackItem))
            nowOperationButton = null
        }
    }
}