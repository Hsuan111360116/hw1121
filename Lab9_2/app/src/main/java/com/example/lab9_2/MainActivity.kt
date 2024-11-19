package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var btnCalculate: Button
    private lateinit var edHeight: EditText
    private lateinit var edWeight: EditText
    private lateinit var edAge: EditText
    private lateinit var tvWeightResult: TextView
    private lateinit var tvFatResult: TextView
    private lateinit var tvBmiResult: TextView
    private lateinit var tvProgress: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var llProgress: LinearLayout
    private lateinit var btnBoy: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupEdgeToEdge()
        bindViews()
        setupListeners()
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindViews() {
        btnCalculate = findViewById(R.id.btnCalculate)
        edHeight = findViewById(R.id.edHeight)
        edWeight = findViewById(R.id.edWeight)
        edAge = findViewById(R.id.edAge)
        tvWeightResult = findViewById(R.id.tvWeightResult)
        tvFatResult = findViewById(R.id.tvFatResult)
        tvBmiResult = findViewById(R.id.tvBmiResult)
        tvProgress = findViewById(R.id.tvProgress)
        progressBar = findViewById(R.id.progressBar)
        llProgress = findViewById(R.id.llProgress)
        btnBoy = findViewById(R.id.btnBoy)
    }

    private fun setupListeners() {
        btnCalculate.setOnClickListener {
            if (validateInputs()) {
                calculateResults()
            }
        }
    }

    private fun validateInputs(): Boolean {
        return when {
            edHeight.text.isEmpty() -> {
                showToast("請輸入身高")
                false
            }
            edWeight.text.isEmpty() -> {
                showToast("請輸入體重")
                false
            }
            edAge.text.isEmpty() -> {
                showToast("請輸入年齡")
                false
            }
            else -> true
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun calculateResults() {
        resetResults()
        initializeProgressBar()

        // 使用協程模擬計算過程
        CoroutineScope(Dispatchers.Main).launch {
            simulateProgress()
            displayResults()
        }
    }

    private fun resetResults() {
        tvWeightResult.text = "標準體重\n無"
        tvFatResult.text = "體脂肪\n無"
        tvBmiResult.text = "BMI\n無"
    }

    private fun initializeProgressBar() {
        progressBar.progress = 0
        tvProgress.text = "0%"
        llProgress.visibility = View.VISIBLE
    }

    private suspend fun simulateProgress() {
        for (progress in 1..100) {
            delay(50) // 模擬耗時操作
            updateProgressBar(progress)
        }
    }

    private fun updateProgressBar(progress: Int) {
        progressBar.progress = progress
        tvProgress.text = "$progress%"
    }

    private suspend fun displayResults() {
        val height = edHeight.text.toString().toDouble()
        val weight = edWeight.text.toString().toDouble()
        val age = edAge.text.toString().toDouble()
        val bmi = calculateBmi(height, weight)
        val (standardWeight, bodyFat) = calculateStandardWeightAndBodyFat(height, bmi, age)

        llProgress.visibility = View.GONE
        tvWeightResult.text = "標準體重 \n${String.format("%.2f", standardWeight)}"
        tvFatResult.text = "體脂肪 \n${String.format("%.2f", bodyFat)}"
        tvBmiResult.text = "BMI \n${String.format("%.2f", bmi)}"
    }

    private fun calculateBmi(height: Double, weight: Double): Double {
        return weight / (height / 100).pow(2)
    }

    private fun calculateStandardWeightAndBodyFat(
        height: Double,
        bmi: Double,
        age: Double
    ): Pair<Double, Double> {
        return if (btnBoy.isChecked) {
            (height - 80) * 0.7 to 1.39 * bmi + 0.16 * age - 19.34
        } else {
            (height - 70) * 0.6 to 1.39 * bmi + 0.16 * age - 9
        }
    }
}
