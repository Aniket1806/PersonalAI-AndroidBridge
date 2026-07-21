package com.personalai.bridge.decision

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.personalai.bridge.actions.ActionEngine
import com.personalai.bridge.memory.Memory

object DecisionEngine {

    private const val TAG = "PersonalAIBridge"

    fun decide(
        packageName: String,
        screenInfo: String,
        targetNode: AccessibilityNodeInfo?
    ) {

        Log.d(TAG, "===== DECISION ENGINE =====")
        Log.d(TAG, "Package: $packageName")
        Log.d(TAG, "Screen: $screenInfo")

        val previousAction = Memory.recall(screenInfo)

        if (previousAction != null) {
            Log.d(TAG, "Memory Hit -> $previousAction")
            ActionEngine.execute(previousAction, targetNode, previousAction)
            return
        }

        when {

            packageName.contains("permissioncontroller", true)
                    && screenInfo.contains("Allow", true) -> {

                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Allow")
            }

            packageName.contains("packageinstaller", true)
                    && screenInfo.contains("Continue", true) -> {

                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Continue")
            }

            packageName.contains("settings", true)
                    && (screenInfo.contains("Allow", true)
                    || screenInfo.contains("Enable", true)
                    || screenInfo.contains("OK", true)) -> {

                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Settings")
            }

            screenInfo.contains("Next", true) -> {
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Next")
            }

            screenInfo.contains("Accept", true) -> {
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Accept")
            }

            screenInfo.contains("Yes", true) -> {
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Yes")
            }

            else -> {
                Log.d(TAG, "No decision for package: $packageName")
            }
        }

        Log.d(TAG, "==========================")
    }
}
