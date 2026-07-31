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

        if (TaskEngine.hasGoal()) {

            val step = TaskEngine.getCurrentStep()

            if (step != null) {

                Log.d(TAG, "Current Task Step: $step")

                when (step) {

                    "CLICK" -> {
                        ActionEngine.execute(
                            "CLICK",
                            targetNode
                        )
                    }

                    "TYPE" -> {
                        val reply =
                            TextGenerator.generateReply(
                                packageName,
                                screenInfo
                            )

                        ActionEngine.execute(
                            "SET_TEXT",
                            targetNode,
                            reply
                        )
                    }

                    "BACK" -> {
                        ActionEngine.execute(
                            "BACK",
                            targetNode
                        )
                    }

                    "HOME" -> {
                        ActionEngine.execute(
                            "HOME",
                            targetNode
                        )
                    }
                }

                TaskEngine.completeStep()
                return
            }
        }

        val previousAction =
            AIMemory.recall(screenInfo)

        if (previousAction != null) {

            Log.d(
                TAG,
                "Memory Found: $previousAction"
            )

            ActionEngine.execute(
                previousAction,
                targetNode
            )

            return
        }

        if (
            targetNode != null &&
            (
                targetNode.isEditable ||
                targetNode.className
                    ?.toString()
                    ?.contains(
                        "EditText",
                        ignoreCase = true
                    ) == true
            )
        ) {

            Log.d(TAG, "Decision: TYPE")

            val reply =
                TextGenerator.generateReply(
                    packageName,
                    screenInfo
                )

            AIMemory.remember(
                screenInfo,
                "SET_TEXT"
            )

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

            screenInfo.contains(
                "Allow",
                ignoreCase = true
            ) ||
            screenInfo.contains(
                "Continue",
                ignoreCase = true
            ) ||
            screenInfo.contains(
                "OK",
                ignoreCase = true
            ) ||
            screenInfo.contains(
                "Next",
                ignoreCase = true
            ) ||
            screenInfo.contains(
                "Accept",
                ignoreCase = true
            ) ||
            screenInfo.contains(
                "Yes",
                ignoreCase = true
            ) -> {

                AIMemory.remember(
                    screenInfo,
                    "CLICK"
                )

                TaskEngine.startGoal(
                    "Press Button",
                    listOf("CLICK")
                )

                ActionEngine.execute(
                    "CLICK",
                    targetNode
                )

                TaskEngine.completeStep()
            }

            else -> {
                Log.d(
                    TAG,
                    "Decision: No Action"
                )
            }
        }

        Log.d(
            TAG,
            "==========================="
        )
    }
}
