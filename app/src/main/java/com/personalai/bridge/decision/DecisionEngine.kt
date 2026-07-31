package com.personalai.bridge.decision

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.personalai.bridge.actions.ActionEngine
import com.personalai.bridge.ai.AIMemory
import com.personalai.bridge.ai.TaskEngine
import com.personalai.bridge.ai.TextGenerator

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

        // Continue current task if one exists
        if (TaskEngine.hasGoal()) {

            val step = TaskEngine.getCurrentStep()

            if (step != null) {

                Log.d(TAG, "Current Task Step: $step")

                when (step) {

                    "CLICK" -> ActionEngine.execute("CLICK", targetNode)

                    "TYPE" -> {
                        val reply = TextGenerator.generate(
                            packageName,
                            screenInfo
                        )

                        ActionEngine.execute(
                            "SET_TEXT",
                            targetNode,
                            reply
                        )
                    }

                    "BACK" -> ActionEngine.execute("BACK", null)

                    "HOME" -> ActionEngine.execute("HOME", null)
                }

                TaskEngine.completeStep()
                return
            }
        }

        val previousAction = AIMemory.recall(screenInfo)

        if (previousAction != null) {
            Log.d(TAG, "Memory Found: $previousAction")
            ActionEngine.execute(previousAction, targetNode)
            return
        }

        // Editable field
        if (
            targetNode != null &&
            (
                targetNode.isEditable ||
                targetNode.className?.toString()?.contains("EditText") == true
            )
        ) {

            Log.d(TAG, "Decision: TYPE")

            val reply = TextGenerator.generate(
                packageName,
                screenInfo
            )

            AIMemory.remember(screenInfo, "SET_TEXT")

            TaskEngine.startGoal(
                "Fill Text",
                listOf("TYPE")
            )

            ActionEngine.execute(
                "SET_TEXT",
                targetNode,
                reply
            )

            TaskEngine.completeStep()
            return
        }

        when {

            screenInfo.contains("Allow", true),
            screenInfo.contains("Continue", true),
            screenInfo.contains("OK", true),
            screenInfo.contains("Next", true),
            screenInfo.contains("Accept", true),
            screenInfo.contains("Yes", true) -> {

                AIMemory.remember(screenInfo, "CLICK")

                TaskEngine.startGoal(
                    "Press Button",
                    listOf("CLICK")
                )

                ActionEngine.execute("CLICK", targetNode)

                TaskEngine.completeStep()
            }

            else -> {
                Log.d(TAG, "Decision: No Action")
            }
        }

        Log.d(TAG, "==========================")
    }
}
