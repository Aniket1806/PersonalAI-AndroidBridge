package com.personalai.bridge.ai

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.personalai.bridge.decision.DecisionEngine

object ScreenAnalyzer {

    private const val TAG = "PersonalAIBridge"

    fun analyze(root: AccessibilityNodeInfo?) {

        if (root == null) return

        val packageName = root.packageName?.toString() ?: "Unknown"

        Log.d(TAG, "========== SCREEN ANALYZER ==========")
        Log.d(TAG, "Package: $packageName")

        val screenInfo = StringBuilder()

        scan(root, screenInfo, packageName)

        Log.d(TAG, "=====================================")
    }

    private fun scan(
        node: AccessibilityNodeInfo?,
        screenInfo: StringBuilder,
        packageName: String
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

            if (
                text.equals("Allow", true) ||
                text.equals("OK", true) ||
                text.equals("Continue", true) ||
                text.equals("Next", true) ||
                text.equals("Accept", true) ||
                text.equals("Yes", true)
            ) {

                DecisionEngine.decide(
                    packageName = packageName,
                    screenInfo = info,
                    targetNode = node
                )
            }
        }

        for (i in 0 until node.childCount) {
            scan(
                node.getChild(i),
                screenInfo,
                packageName
            )
        }
    }
}
