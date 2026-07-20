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

        for (target in targetButtons) {
            if (
                text.equals(target, ignoreCase = true) ||
                desc.equals(target, ignoreCase = true)
            ) {
                Log.d(TAG, "Target Button Found: $target")
            }
        }

        for (i in 0 until node.childCount) {
            scan(node.getChild(i))
        }
    }
}