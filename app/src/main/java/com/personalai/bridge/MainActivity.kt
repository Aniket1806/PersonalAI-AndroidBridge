package com.personalai.bridge

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.personalai.bridge.service.BridgeService

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.startBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        val statusText = findViewById<TextView>(R.id.statusText)

        startBtn.setOnClickListener {
            val intent = Intent(this, BridgeService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }

            statusText.text = "Status : Bridge Running"
        }

        stopBtn.setOnClickListener {
            stopService(Intent(this, BridgeService::class.java))
            statusText.text = "Status : Bridge Stopped"
        }
    }
}
