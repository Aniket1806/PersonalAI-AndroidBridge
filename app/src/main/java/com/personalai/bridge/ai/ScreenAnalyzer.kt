package com.personalai.bridge.ai

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

object ScreenAnalyzer {

    private const val TAG = "PersonalAIBridge"

    fun analyze(root: AccessibilityNodeInfo?) {

        if (root == null) return

        val packageName = root.packageName?.toString() ?: "Unknown"

        Log.d(TAG, "========== SCREEN ANALYSIS ==========")
        Log.d(TAG, "Package: $packageName")

        scan(root)

        Log.d(TAG, "====================================")
    }

    private fun scan(node: AccessibilityNodeInfo?) {

        if (node == null) return

        val text = node.text?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""
        val id = node.viewIdResourceName ?: "No ID"
        val className = node.className?.toString() ?: "Unknown"

        if (
            text.isNotEmpty() ||
            desc.isNotEmpty()
        ) {

            Log.d(
                TAG,
                "Text: $text | Desc: $desc | ID: $id | Class: $className"
            )
        }

        for (i in 0 until node.childCount) {
            scan(node.getChild(i))
        }
    }
}
