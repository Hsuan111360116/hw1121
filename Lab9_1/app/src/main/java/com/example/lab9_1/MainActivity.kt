package com.example.lab9_1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var progressRabbit = 0
    private var progressTurtle = 0

    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    // 主執行緒的 Handler，處理 UI 更新
    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            RABBIT -> updateProgress(sbRabbit, progressRabbit, "兔子")
            TURTLE -> updateProgress(sbTurtle, progressTurtle, "烏龜")
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupEdgeToEdge()

        initializeViews()
        btnStart.setOnClickListener { startRace() }
    }

    /**
     * 初始化元件
     */
    private fun initializeViews() {
        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)
    }

    /**
     * 設置全螢幕顯示並處理系統邊距
     */
    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * 開始比賽
     */
    private fun startRace() {
        btnStart.isEnabled = false
        resetProgress()

        thread { runRabbit() }
        thread { runTurtle() }
    }

    /**
     * 重設進度條和變數
     */
    private fun resetProgress() {
        progressRabbit = 0
        progressTurtle = 0
        sbRabbit.progress = 0
        sbTurtle.progress = 0
    }

    /**
     * 更新進度條並檢查比賽狀態
     */
    private fun updateProgress(seekBar: SeekBar, progress: Int, racer: String) {
        seekBar.progress = progress
        if (progress >= 100) {
            showToast("$racer 勝利")
            finishRace()
        }
    }

    /**
     * 結束比賽並重置按鈕狀態
     */
    private fun finishRace() {
        btnStart.isEnabled = true
    }

    /**
     * 顯示 Toast 訊息
     */
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 模擬兔子賽跑
     */
    private fun runRabbit() {
        val sleepProbability = arrayOf(true, true, false)
        while (progressRabbit < 100 && progressTurtle < 100) {
            Thread.sleep(100)
            if (sleepProbability.random()) {
                Thread.sleep(300)
            }
            progressRabbit += 3
            handler.sendMessage(handler.obtainMessage(RABBIT))
        }
    }

    /**
     * 模擬烏龜賽跑
     */
    private fun runTurtle() {
        while (progressTurtle < 100 && progressRabbit < 100) {
            Thread.sleep(100)
            progressTurtle += 1
            handler.sendMessage(handler.obtainMessage(TURTLE))
        }
    }

    companion object {
        private const val RABBIT = 1
        private const val TURTLE = 2
    }
}
