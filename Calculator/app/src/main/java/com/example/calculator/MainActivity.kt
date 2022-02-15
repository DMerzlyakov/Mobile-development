package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_0.setOnClickListener { update("0", true) }
        bt_1.setOnClickListener { update("1", true) }
        bt_2.setOnClickListener { update("2", true) }
        bt_3.setOnClickListener { update("3", true) }
        bt_4.setOnClickListener { update("4", true) }
        bt_5.setOnClickListener { update("5", true) }
        bt_6.setOnClickListener { update("6", true) }
        bt_7.setOnClickListener { update("7", true) }
        bt_8.setOnClickListener { update("8", true) }
        bt_9.setOnClickListener { update("9", true) }

        bt_dot.setOnClickListener { update(".", true) }
        bt_sum.setOnClickListener { update("+", false) }
        bt_minus.setOnClickListener { update("-", false) }
        bt_mult.setOnClickListener { update("*", false) }
        bt_divide.setOnClickListener { update("/", false) }
        bt_open.setOnClickListener { update("(", false) }
        bt_close.setOnClickListener { update(")", false) }

        bt_eq.setOnClickListener {
            try {

                val answer = ExpressionBuilder(field_expression.text.toString()).build().evaluate()
                field_res.text = answer.toString()

            } catch (e: Exception) {
                field_res.text = "error"
            }
        }

        bt_c.setOnClickListener {
            field_res.text = ""
            field_expression.text = ""
        }

        bt_back.setOnClickListener {
            var expression = field_expression.text.toString()
            expression = expression.substring(0, expression.length - 1)
            field_expression.text = expression
        }

    }

    private fun update(data: String, needToClear: Boolean) {

        if (needToClear) {
            field_res.text = ""
            field_expression.append(data)
        } else {
            field_expression.append(data)
        }


    }
}