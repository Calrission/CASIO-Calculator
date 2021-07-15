package com.example.calculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    var nowSysButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchThemeMode(!getSharedPreferences("0", 0).getBoolean("night_mode", false))
        
        val numClick = View.OnClickListener{
            addNewChar((it as TextView).text.toString())
        }

        val sysClick = View.OnClickListener {
            addNewChar((it as TextView).text.toString())
            toNormalNowButton()
            nowSysButton = it
            nowSysButton!!.setBackgroundColor(getColor(R.color.colorAccentButton))
        }

        for (i in arrayListOf(num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, procent, dot))
            i.setOnClickListener(numClick)

        for (i in arrayListOf(deleny, minus, plus, multy))
            i.setOnClickListener(sysClick)

        c.setOnClickListener {
            main_text.text = ""
            second_text.text = ""
            toNormalNowButton()
        }

        card_so.setOnClickListener {
            try {
                val match = if (main_text.text.isNotEmpty()) {
                    ExpressionBuilder(main_text.text.toString()).build()
                }else {
                    ExpressionBuilder(second_text.text.toString()).build()
                }
                val result = match!!.evaluate()
                second_text.text = main_text.text.toString()
                if (".0" in result.toString()){
                    main_text.text = result.toLong().toString()
                }else{
                    main_text.text = result.toString()
                }
                toNormalNowButton()
            }catch (e: Exception){
                Snackbar.make(so, "Ошибка!\n${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        sun_panel.setOnClickListener {
            switchThemeMode()
        }

        delete_num.setOnClickListener {
            try {
                val delete_char = main_text.text.toString().last().toString()
                main_text.text =
                    main_text.text.toString().substring(0, main_text.text.toString().length - 1)
                if (delete_char in arrayListOf("+", "-", "/", "*"))
                    toNormalNowButton()
            }catch (e: Exception){

            }
        }
    }

    private fun toNormalNowButton(){
        if (nowSysButton != null)
            nowSysButton!!.setBackgroundColor(getColor(R.color.colorBackItem))
    }

    private fun addNewChar(char: String){
        main_text.append(char)
    }

    private fun switchThemeMode(now_theme_night: Boolean = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
        if (now_theme_night)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        getSharedPreferences("0", 0).edit()
            .putBoolean("night_mode", !now_theme_night)
            .apply()
    }
}