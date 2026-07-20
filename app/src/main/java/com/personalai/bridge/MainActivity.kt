package com.personalai.bridge

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.startBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        val statusText = findViewById<TextView>(R.id.statusText)

        startBtn.setOnClickListener {
            statusText.text = "Status : Bridge Running"
        }

        stopBtn.setOnClickListener {
            statusText.text = "Status : Bridge Stopped"
        }
    }
}
