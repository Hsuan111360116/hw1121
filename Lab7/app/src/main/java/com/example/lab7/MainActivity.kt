package com.example.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupEdgeToEdge()

        // 初始化元件
        val spinner = findViewById<Spinner>(R.id.spinner)
        val listView = findViewById<ListView>(R.id.listView)
        val gridView = findViewById<GridView>(R.id.gridView)

        // 初始化數據
        val fruitData = loadFruitData()
        val countData = fruitData.indices.map { "${it + 1}個" }

        // 設定 Spinner Adapter
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countData)

        // 設定 GridView
        gridView.numColumns = 3
        gridView.adapter = MyAdapter(this, fruitData, R.layout.adapter_vertical)

        // 設定 ListView
        listView.adapter = MyAdapter(this, fruitData, R.layout.adapter_horizontal)
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
     * 加載水果數據
     * @return List<Item>
     */
    private fun loadFruitData(): List<Item> {
        val fruitList = mutableListOf<Item>()
        val priceRange = 10..100 // 價格範圍
        val imageArray = resources.obtainTypedArray(R.array.image_list)

        try {
            for (index in 0 until imageArray.length()) {
                val photo = imageArray.getResourceId(index, 0)
                val name = "水果${index + 1}"
                val price = priceRange.random()
                fruitList.add(Item(photo, name, price))
            }
        } finally {
            imageArray.recycle() // 確保釋放資源
        }

        return fruitList
    }
}
