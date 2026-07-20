package com.personalai.bridge.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

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
            scanNode(root)
        }
    }

    private fun scanNode(node: AccessibilityNodeInfo?) {

        if (node == null) return

        val text = node.text?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""

        if (text.isNotEmpty() || desc.isNotEmpty()) {
            Log.d(
                TAG,
                "Node -> Text: $text | Desc: $desc | Class: ${node.className}"
            )
        }

        for (i in 0 until node.childCount) {
            scanNode(node.getChild(i))
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
