package com.personalai.bridge.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.personalai.bridge.ai.ScreenAnalyzer
import com.personalai.bridge.decision.DecisionEngine

class BridgeAccessibilityService : AccessibilityService() {

    companion object {

        private const val TAG = "PersonalAIBridge"

        var instance: BridgeAccessibilityService? = null
            private set

        fun globalBack(): Boolean {
            return instance?.performGlobalAction(
                AccessibilityService.GLOBAL_ACTION_BACK
            ) ?: false
        }

        fun globalHome(): Boolean {
            return instance?.performGlobalAction(
                AccessibilityService.GLOBAL_ACTION_HOME
            ) ?: false
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "Accessibility Service Connected")
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

        val root = rootInActiveWindow ?: return

        scanNode(root)

        ScreenAnalyzer.analyze(root)

        DecisionEngine.decide(
            packageName = packageName,
            screenInfo = "Accessibility Event",
            targetNode = root
        )
    }

    private fun scanNode(node: AccessibilityNodeInfo?) {

        if (node == null) return

        val text = node.text?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""

        if (text.isNotEmpty() || desc.isNotEmpty()) {
            Log.d(
                TAG,
                "Node -> Text: $text | Desc: $desc"
            )
        }

        for (i in 0 until node.childCount) {
            scanNode(node.getChild(i))
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service Interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
