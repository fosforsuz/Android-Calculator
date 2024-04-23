package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

// This is the main activity class for the application
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var newOp = true

    // This function is called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        // Call the superclass method is mandatory for lifecycle callbacks
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun percentage(view: View) {
        try {
            val number: Double = binding.calculation.text.toString().toDouble() / 100
            binding.calculation.text = number.toString()
            newOp = true
        } catch (ex: Exception) {
            Toast.makeText(this, "Geçersiz hesaplama", Toast.LENGTH_LONG).show()
        }
    }

    fun process(view: View) {
        val button = view as Button
        var calculationText = binding.calculation.text.toString()

        if (calculationText.isEmpty())
            calculationText = "0"

        when (button.id) {
            binding.div.id -> calculationText += "/"
            binding.multiply.id -> calculationText += "x"
            binding.minus.id -> calculationText += "-"
            binding.plus.id -> calculationText += "+"
        }

        binding.calculation.text = calculationText
    }

    private fun calculate() {
        try {
            val equation = binding.calculation.text.toString()
            val expressions = splitExpression(equation)
            val numberStack = mutableListOf<Double>()
            val operatorStack = mutableListOf<String>()

            for (exp in expressions) {
                when {
                    exp.isNumber() -> numberStack.add(exp.toDouble())
                    exp.isOperator() -> {
                        while (operatorStack.isNotEmpty() && comparePrecedence(exp, operatorStack.last())) {
                            calculateAndPushResult(numberStack, operatorStack)
                        }
                        operatorStack.add(exp)
                    }
                    else -> throw IllegalArgumentException("Invalid Data")
                }
            }

            while (operatorStack.isNotEmpty()) {
                calculateAndPushResult(numberStack, operatorStack)
            }

            val result = numberStack.first()
            if (result.isNaN()) {
                Toast.makeText(this, "Error: Division by zero", Toast.LENGTH_LONG).show()
            } else {
                "=${result}".also { binding.result.text = it }
                binding.result.setTextColor(Color.parseColor("#616161"))
                binding.result.textSize = 20F
            }
        } catch (e: Exception) {
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    private fun calculateAndPushResult(numberStack: MutableList<Double>, operatorStack: MutableList<String>) {
        val operator = operatorStack.removeLast()
        val operand2 = numberStack.removeLast()
        val operand1 = numberStack.removeLast()
        numberStack.add(calculateOperation(operand1, operand2, operator))
    }

    private fun calculateOperation(operand1: Double, operand2: Double, operator: String): Double {
        return when (operator[0]) {
            '-' -> operand1 - operand2
            '/' -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
            '+' -> operand1 + operand2
            'x' -> operand1 * operand2
            else -> throw IllegalArgumentException("Invalid Operator")
        }
    }

    private fun comparePrecedence(operator1: String, operator2: String): Boolean {
        return precedence(operator1) <= precedence(operator2)
    }

    private fun precedence(opr: String): Int {
        return if (opr == "/" || opr == "x") 2 else 1
    }

    private fun String.isNumber(): Boolean {
        return this.toDoubleOrNull() != null
    }

    private fun String.isOperator(): Boolean {
        return this.toCharArray().firstOrNull() in setOf('x', '-', '/', '+')
    }

    private fun splitExpression(expression: String): List<String> {
        val parts = mutableListOf<String>()
        var decimal = ""

        for (i in expression.indices) {
            val char = expression[i]
            if (char.isDigit() || char == '.') {
                decimal += char
            } else {
                if (decimal.isNotBlank()) {
                    parts.add(decimal)
                    decimal = ""
                }
                parts.add(char.toString())
            }
        }

        if (decimal.isNotBlank()) {
            parts.add(decimal)
        }

        return parts
    }

    fun numberButton(view: View) {
        if (newOp) {
            binding.calculation.text = ""
            newOp = false
        }
        val button = view as Button
        var calculationText = binding.calculation.text.toString()

        when (button.id) {
            binding.zero.id -> {
                calculationText += "0"
            }

            binding.one.id -> {
                calculationText += "1"
            }

            binding.two.id -> {
                calculationText += "2"
            }

            binding.three.id -> {
                calculationText += "3"
            }

            binding.four.id -> {
                calculationText += "4"
            }

            binding.five.id -> {
                calculationText += "5"
            }

            binding.six.id -> {
                calculationText += "6"
            }

            binding.seven.id -> {
                calculationText += "7"
            }

            binding.eight.id -> {
                calculationText += "8"
            }

            binding.nine.id -> {
                calculationText += "9"
            }

            binding.dot.id -> {
                if (calculationText.isEmpty()) {
                    calculationText += "0."
                } else if (!calculationText.contains(".")) {
                    calculationText += "."
                }
            }
        }

        binding.calculation.text = calculationText
        calculate()
    }

    fun equalOnClick(view: View) {
        binding.result.setTextColor(Color.parseColor("#FFFFFF"))
        binding.calculation.textSize = 36F
        binding.result.textSize = 48F
    }

    fun clearAll(view: View) {
        binding.calculation.text = ""
        binding.result.text = "0"
        binding.result.setTextColor(Color.parseColor("#FFFFFF"))
        newOp = true
    }

    fun clearFunc(view: View) {
        var calculation = binding.calculation.text.toString()
        if (calculation.isNotEmpty()) {
            calculation = calculation.substring(0, calculation.length - 1)
            binding.calculation.text = calculation
        }
    }

}