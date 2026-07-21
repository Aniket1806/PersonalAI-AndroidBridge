package com.personalai.bridge.ai

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.personalai.bridge.decision.DecisionEngine

object ScreenAnalyzer {

    private const val TAG = "PersonalAIBridge"

    fun analyze(root: AccessibilityNodeInfo?) {

        if (root == null) return

        val packageName = root.packageName?.toString() ?: "Unknown"

        Log.d(TAG, "========== SCREEN ANALYSIS ==========")
        Log.d(TAG, "Package: $packageName")

        val screenInfo = StringBuilder()

        scan(root, screenInfo)

        DecisionEngine.decide(
            packageName,
            screenInfo.toString()
        )

        Log.d(TAG, "====================================")
    }

    private fun scan(
        node: AccessibilityNodeInfo?,
        screenInfo: StringBuilder
    ) {

        if (node == null) return

        val text = node.text?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""
        val id = node.viewIdResourceName ?: "No ID"
        val className = node.className?.toString() ?: "Unknown"

        if (text.isNotEmpty() || desc.isNotEmpty()) {

            val info =
                "Text: $text | Desc: $desc | ID: $id | Class: $className"

            Log.d(TAG, info)

            screenInfo.append(info).append("\n")
        }

        for (i in 0 until node.childCount) {
            scan(node.getChild(i), screenInfo)
        }
    }
}
