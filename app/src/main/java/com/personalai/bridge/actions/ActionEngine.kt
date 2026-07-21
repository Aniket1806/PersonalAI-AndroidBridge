package com.personalai.bridge.actions

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo

object ActionEngine {

    private const val TAG = "PersonalAIBridge"

    private val targetButtons = listOf(
        "Allow",
        "OK",
        "Continue",
        "Next",
        "Accept",
        "Yes"
    )

    fun scan(node: AccessibilityNodeInfo?) {
        if (node == null) return

        val text = node.text?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""
        val id = node.viewIdResourceName ?: "No ID"
        val className = node.className?.toString() ?: "Unknown"
        val packageName = node.packageName?.toString() ?: "Unknown"

        Log.d(
            TAG,
            "Node -> Text: $text | Desc: $desc | ID: $id | Class: $className | Package: $packageName | Clickable: ${node.isClickable} | Enabled: ${node.isEnabled}"
        )

        for (target in targetButtons) {
            if (
                text.equals(target, ignoreCase = true) ||
                desc.equals(target, ignoreCase = true)
            ) {

                Log.d(TAG, "Target Button Found: $target")

                if (node.isClickable && node.isEnabled) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    Log.d(TAG, "Clicked: $target")
                }
            }
        }

        for (i in 0 until node.childCount) {
            scan(node.getChild(i))
        }
    }
}
