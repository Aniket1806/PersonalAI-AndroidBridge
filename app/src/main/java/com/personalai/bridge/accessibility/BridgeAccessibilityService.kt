package com.personalai.bridge.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class BridgeAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "PersonalAIBridge"
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        val packageName = event.packageName?.toString() ?: "Unknown"
        val className = event.className?.toString() ?: "Unknown"
        val eventType = event.eventType

        Log.d(
            TAG,
            "Package: $packageName | Class: $className | Event: $eventType"
        )

        val root = rootInActiveWindow

        if (root != null) {
            Log.d(TAG, "Current Screen: ${root.packageName}")

            val text = root.text
            if (text != null) {
                Log.d(TAG, "Root Text: $text")
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service Connected")
    }
}
