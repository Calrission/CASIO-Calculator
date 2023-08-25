package com.example.calculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var nowOperationButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val numButtons = arrayListOf(
            binding.num0, binding.num1, binding.num2,
            binding.num3, binding.num4, binding.num5,
            binding.num6, binding.num7, binding.num8,
            binding.num9, binding.procent, binding.dot
        )
        
        val operationButtons = arrayListOf(
            binding.deleny, binding.minus,
            binding.plus, binding.multy
        )
        
        numButtons.forEach{ btn ->
            btn.setOnClickListener{
                addNewChar(btn.text.toString())
            }
        }
        
        operationButtons.forEach{ btn ->
            btn.setOnClickListener{
                addNewChar(btn.text.toString())
                toNormalNowButton()
                nowOperationButton = btn
                nowOperationButton!!.setBackgroundColor(getColorAttr(R.attr.colorAccentButton))
            }
        }

        switchThemeMode(!getSharedPreferences("0", 0).getBoolean("night_mode", false))

        binding.c.setOnClickListener {
            binding.mainText.text = ""
            binding.secondText.text = ""
            toNormalNowButton()
        }

        binding.cardSo.setOnClickListener {
            try {
                val match = if (binding.mainText.text.isNotEmpty()) {
                    ExpressionBuilder(binding.mainText.text.toString()).build()
                }else {
                    ExpressionBuilder(binding.secondText.text.toString()).build()
                }
                val result = match!!.evaluate()
                binding.secondText.text = binding.mainText.text.toString()
                if (".0" in result.toString()){
                    binding.mainText.text = result.toLong().toString()
                }else{
                    binding.mainText.text = result.toString()
                }
                toNormalNowButton()
            }catch (e: Exception){
                Snackbar.make(binding.so, "Ошибка!\n${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.sunPanel.setOnClickListener {
            switchThemeMode()
        }

        binding.deleteNum.setOnClickListener {
            try {
                val deleteChar = binding.mainText.text.toString().last().toString()
                binding.mainText.text =
                    binding.mainText.text.toString().substring(0, binding.mainText.text.toString().length - 1)
                if (deleteChar in arrayListOf("+", "-", "/", "*")) toNormalNowButton()
            }catch (_: Exception){}
        }

        loadState()
    }

    private fun toNormalNowButton(){
        if (nowOperationButton != null)
            nowOperationButton!!.setBackgroundColor(getColorAttr(R.attr.colorBackItem))
    }

    private fun addNewChar(char: String){
        binding.mainText.append(char)
    }

    private fun getColorAttr(idColor: Int): Int{
        val value = TypedValue()
        theme.resolveAttribute(idColor, value, true)
        return value.data
    }

    private fun switchThemeMode(
        nowThemeNight: Boolean = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    ){
        saveState()
        if (nowThemeNight)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        getSharedPreferences("0", 0).edit()
            .putBoolean("night_mode", !nowThemeNight)
            .apply()
    }

    private fun saveState(){
        val sh = getSharedPreferences("0", 0)
        val themeChange = sh.getBoolean("theme_change", false)
        if (!themeChange) {
            sh.edit()
                .putString("binding.mainText", binding.mainText.text.toString())
                .putString("binding.secondText", binding.secondText.text.toString())
                .putBoolean("theme_change", true)
                .apply()
            if (nowOperationButton != null) {
                sh.edit()
                    .putInt("id_system_button", nowOperationButton!!.id)
                    .apply()
            }else{
                sh.edit()
                    .putInt("id_system_button", 0)
                    .apply()
            }
        }
    }

    private fun loadState(){
        val sh = getSharedPreferences("0", 0)
        val themeChange = sh.getBoolean("theme_change", false)
        if (themeChange) {
            val mainTextValue = sh.getString("binding.mainText", "")
            val secondTextValue = sh.getString("binding.secondText", "")
            val idSystemButton = sh.getInt("id_system_button", 0)
            binding.mainText.text = mainTextValue
            binding.secondText.text = secondTextValue
            if (idSystemButton != 0) {
                val textView = findViewById<TextView>(idSystemButton)
                textView.setBackgroundColor(getColorAttr(R.attr.colorAccentButton))
                nowOperationButton = textView
            }
            sh.edit()
                .putBoolean("theme_change", false)
//                .putString("binding.mainText", "")
//                .putString("binding.secondText", "")
//                .putInt("id_system_button", 0)
                .apply()
        }
    }
}