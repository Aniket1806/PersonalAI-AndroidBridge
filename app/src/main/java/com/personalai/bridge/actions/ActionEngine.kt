package com.personalai.bridge.actions

import android.accessibilityservice.AccessibilityService
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
            "Node -> Text: $text | Desc: $desc | ID: $id | Class: $className | Package: $packageName"
        )

        for (target in targetButtons) {
            if (
                text.equals(target, true) ||
                desc.equals(target, true)
            ) {
                execute("CLICK", node, target)
            }
        }

        for (i in 0 until node.childCount) {
            scan(node.getChild(i))
        }
    }

    fun execute(
        action: String,
        node: AccessibilityNodeInfo?,
        target: String = ""
    ) {

        when (action) {

            "CLICK" -> {

                if (node == null) return

                var clicked = false

                if (node.isClickable && node.isEnabled) {
                    clicked = node.performAction(
                        AccessibilityNodeInfo.ACTION_CLICK
                    )
                }

                if (!clicked) {
                    val parent = node.parent
                    if (parent != null && parent.isClickable) {
                        clicked = parent.performAction(
                            AccessibilityNodeInfo.ACTION_CLICK
                        )
                    }
                }

                Log.d(TAG, "CLICK [$target] -> $clicked")
            }

            "LONG_CLICK" -> {

                if (node == null) return

                val result = node.performAction(
                    AccessibilityNodeInfo.ACTION_LONG_CLICK
                )

                Log.d(TAG, "LONG_CLICK [$target] -> $result")
            }

            "SCROLL_FORWARD" -> {

                if (node == null) return

                val result = node.performAction(
                    AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                )

                Log.d(TAG, "SCROLL_FORWARD -> $result")
            }

            "SCROLL_BACKWARD" -> {

                if (node == null) return

                val result = node.performAction(
                    AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                )

                Log.d(TAG, "SCROLL_BACKWARD -> $result")
            }

            "BACK" -> {
                Log.d(TAG, "BACK action requested")
            }

            "HOME" -> {
                Log.d(TAG, "HOME action requested")
            }

            else -> {
                Log.d(TAG, "Unknown Action: $action")
            }
        }
    }
}
