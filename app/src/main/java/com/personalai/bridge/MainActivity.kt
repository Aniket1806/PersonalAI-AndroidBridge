package com.personalai.bridge

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.personalai.bridge.ai.TextGenerator
import com.personalai.bridge.service.BridgeService

class MainActivity : Activity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById<Button>(R.id.startBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        val accessibilityBtn =
            findViewById<Button>(R.id.accessibilityBtn)

        val sendBtn =
            findViewById<Button>(R.id.sendBtn)

        val messageInput =
            findViewById<EditText>(R.id.messageInput)

        val chatText =
            findViewById<TextView>(R.id.chatText)

        statusText =
            findViewById(R.id.statusText)

        updateStatus()

        startBtn.setOnClickListener {

            val intent = Intent(
                this,
                BridgeService::class.java
            )

            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O
            ) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }

            getSharedPreferences(
                "bridge_prefs",
                MODE_PRIVATE
            ).edit()
                .putBoolean(
                    "bridge_running",
                    true
                )
                .apply()

            statusText.text =
                "Status : Bridge Running"
        }

        stopBtn.setOnClickListener {

            stopService(
                Intent(
                    this,
                    BridgeService::class.java
                )
            )

            getSharedPreferences(
                "bridge_prefs",
                MODE_PRIVATE
            ).edit()
                .putBoolean(
                    "bridge_running",
                    false
                )
                .apply()

            statusText.text =
                "Status : Bridge Stopped"
        }

        accessibilityBtn.setOnClickListener {

            startActivity(
                Intent(
                    Settings.ACTION_ACCESSIBILITY_SETTINGS
                )
            )
        }

        sendBtn.setOnClickListener {

            val message =
                messageInput.text
                    .toString()
                    .trim()

            if (message.isEmpty()) {
                return@setOnClickListener
            }

            val generatedReply =
                TextGenerator.generateReply(
                    packageName,
                    message
                )

            val reply =
                generatedReply.ifBlank {
                    "I received your message: $message"
                }

            chatText.append(
                "\n\nYou: $message\n" +
                "PersonalAI: $reply"
            )

            messageInput.setText("")
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {

        val running =
            getSharedPreferences(
                "bridge_prefs",
                MODE_PRIVATE
            ).getBoolean(
                "bridge_running",
                false
            )

        statusText.text =
            if (running) {
                "Status : Bridge Running"
            } else {
                "Status : Bridge Stopped"
            }
    }
}
