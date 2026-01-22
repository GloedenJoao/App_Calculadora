package com.example.calculadora

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var expressionView: EditText
    private lateinit var resultView: TextView
    private var expressionText: String = ""
    private var lastAnswer: Double = 0.0
    private var isUpdatingExpression = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expressionView = findViewById(R.id.editExpression)
        resultView = findViewById(R.id.textResult)
        expressionView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdatingExpression) {
                    expressionText = s?.toString().orEmpty()
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        val buttonMap = mapOf(
            R.id.button0 to "0",
            R.id.button1 to "1",
            R.id.button2 to "2",
            R.id.button3 to "3",
            R.id.button4 to "4",
            R.id.button5 to "5",
            R.id.button6 to "6",
            R.id.button7 to "7",
            R.id.button8 to "8",
            R.id.button9 to "9",
            R.id.buttonPlus to "+",
            R.id.buttonMinus to "-",
            R.id.buttonMultiply to "×",
            R.id.buttonDivide to "÷",
            R.id.buttonDot to ".",
            R.id.buttonOpenParen to "(",
            R.id.buttonCloseParen to ")",
            R.id.buttonAns to "Ans"
        )

        buttonMap.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener { appendToExpression(value) }
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            expressionText = ""
            updateExpression()
            updateResult("=")
        }

        findViewById<Button>(R.id.buttonBackspace).setOnClickListener {
            if (expressionText.isNotEmpty()) {
                expressionText = expressionText.dropLast(1)
                updateExpression()
            }
        }

        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            if (expressionText.isBlank()) {
                return@setOnClickListener
            }
            try {
                val result = ExpressionEvaluator.evaluate(expressionText, lastAnswer)
                lastAnswer = result
                updateResult("= ${formatResult(result)}")
            } catch (exception: IllegalArgumentException) {
                updateResult("= Error")
            }
        }

        updateExpression()
        updateResult("=")
    }

    private fun appendToExpression(value: String) {
        expressionText += value
        updateExpression()
    }

    private fun updateExpression() {
        isUpdatingExpression = true
        expressionView.setText(expressionText)
        if (expressionText.isNotEmpty()) {
            expressionView.setSelection(expressionText.length)
        }
        isUpdatingExpression = false
    }

    private fun updateResult(text: String) {
        resultView.text = text
    }

    private fun formatResult(value: Double): String {
        val rounded = value.toLong()
        return if (value == rounded.toDouble()) {
            rounded.toString()
        } else {
            String.format("%.6f", value).trimEnd('0').trimEnd('.')
        }
    }
}
