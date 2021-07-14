package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    var nowSysButton: TextView? = null

    lateinit var textForNums: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textForNums = main_text

        val numClick = View.OnClickListener{
            addNewChar((it as TextView).text.toString())
        }

        val sysClick = View.OnClickListener {
            addNewChar((it as TextView).text.toString())
            if (nowSysButton != null){
                nowSysButton!!.setBackgroundColor(getColor(R.color.colorBackItem))
            }
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
            textForNums = main_text
        }

        card_so.setOnClickListener {
            try {
                val match = ExpressionBuilder(textForNums.text.toString()).build()
                val result = match.evaluate()
                if (second_text.text.toString().isEmpty()){
                    second_text.text = textForNums.text.toString()
                    textForNums = second_text
                }
                if (".0" in result.toString()){
                    main_text.text = result.toLong().toString()
                }else{
                    main_text.text = result.toString()
                }
                if (nowSysButton != null)
                    nowSysButton!!.setBackgroundColor(getColor(R.color.colorBackItem))
            }catch (e: Exception){
                Snackbar.make(so, "Ошибка!\n${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        delete_num.setOnClickListener {
            try {
                val delete_char = textForNums.text.toString().last().toString()
                textForNums.text =
                    textForNums.text.toString().substring(0, textForNums.text.toString().length - 1)
                if (delete_char in arrayListOf("+", "-", "/", "*") && nowSysButton != null)
                    nowSysButton!!.setBackgroundColor(getColor(R.color.colorBackItem))
            }catch (e: Exception){

            }
        }
    }

    private fun addNewChar(char: String){
        textForNums.append(char)
    }
}