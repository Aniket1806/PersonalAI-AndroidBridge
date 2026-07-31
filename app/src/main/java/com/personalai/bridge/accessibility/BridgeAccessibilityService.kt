package com.personalai.bridge.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.personalai.bridge.ai.ScreenAnalyzer
import com.personalai.bridge.decision.DecisionEngine

class BridgeAccessibilityService :
    AccessibilityService() {

    companion object {

        private const val TAG =
            "PersonalAIBridge"

        var instance:
            BridgeAccessibilityService? = null
            private set

        fun globalBack(): Boolean {
            return instance?.performGlobalAction(
                AccessibilityService
                    .GLOBAL_ACTION_BACK
            ) ?: false
        }

        fun globalHome(): Boolean {
            return instance?.performGlobalAction(
                AccessibilityService
                    .GLOBAL_ACTION_HOME
            ) ?: false
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        instance = this

        Log.d(
            TAG,
            "Accessibility Service Connected"
        )
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {

        if (event == null) return

        val packageName =
            event.packageName
                ?.toString()
                ?: return

        if (
            packageName ==
            applicationContext.packageName
        ) {
            Log.d(
                TAG,
                "Ignoring PersonalAI app event"
            )
            return
        }

        val root =
            rootInActiveWindow
                ?: return

        Log.d(
            TAG,
            "Processing: $packageName"
        )

        ScreenAnalyzer.analyze(root)

        DecisionEngine.decide(
            packageName = packageName,
            screenInfo =
                buildScreenInfo(root),
            targetNode = root
        )
    }

    private fun buildScreenInfo(
        node: AccessibilityNodeInfo?
    ): String {

        if (node == null) {
            return ""
        }

        val result =
            StringBuilder()

        collectText(
            node,
            result
        )

        return result
            .toString()
            .take(4000)
    }

    private fun collectText(
        node: AccessibilityNodeInfo?,
        result: StringBuilder
    ) {

        if (node == null) return

        val text =
            node.text
                ?.toString()
                ?.trim()
                .orEmpty()

        val description =
            node.contentDescription
                ?.toString()
                ?.trim()
                .orEmpty()

        if (text.isNotEmpty()) {
            result
                .append(text)
                .append('\n')
        }

        if (
            description.isNotEmpty() &&
            description != text
        ) {
            result
                .append(description)
                .append('\n')
        }

        for (
            i in 0 until
            node.childCount
        ) {
            collectText(
                node.getChild(i),
                result
            )
        }
    }

    override fun onInterrupt() {

        instance = null

        Log.d(
            TAG,
            "Accessibility Service Interrupted"
        )
    }

    override fun onDestroy() {

        instance = null

        super.onDestroy()
    }
}
