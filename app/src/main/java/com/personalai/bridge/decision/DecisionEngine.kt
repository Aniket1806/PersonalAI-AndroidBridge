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
        Log.d(TAG, "Screen Info: $screenInfo")

        // Check memory first
        val previousAction = Memory.recall(screenInfo)

        if (previousAction != null) {
            Log.d(TAG, "Memory Found: $previousAction")
            ActionEngine.execute(previousAction, targetNode, previousAction)
            return
        }

        when {

            screenInfo.contains("Allow", true) -> {
                Log.d(TAG, "Decision: Click Allow")
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Allow")
            }

            screenInfo.contains("Continue", true) -> {
                Log.d(TAG, "Decision: Click Continue")
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Continue")
            }

            screenInfo.contains("OK", true) -> {
                Log.d(TAG, "Decision: Click OK")
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "OK")
            }

            screenInfo.contains("Next", true) -> {
                Log.d(TAG, "Decision: Click Next")
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Next")
            }

            screenInfo.contains("Accept", true) -> {
                Log.d(TAG, "Decision: Click Accept")
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Accept")
            }

            screenInfo.contains("Yes", true) -> {
                Log.d(TAG, "Decision: Click Yes")
                Memory.remember(screenInfo, "CLICK")
                ActionEngine.execute("CLICK", targetNode, "Yes")
            }

            else -> {
                Log.d(TAG, "Decision: No Action")
            }
        }

        Log.d(TAG, "==========================")
    }
}
