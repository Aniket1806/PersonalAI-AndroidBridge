package com.personalai.bridge.decision

import android.util.Log

object DecisionEngine {

    private const val TAG = "PersonalAIBridge"

    fun decide(
        packageName: String,
        screenInfo: String
    ) {

        Log.d(TAG, "===== DECISION ENGINE =====")
        Log.d(TAG, "Package: $packageName")
        Log.d(TAG, "Screen Info: $screenInfo")

        when {

            screenInfo.contains("Allow", true) ->
                Log.d(TAG, "Decision: Click Allow")

            screenInfo.contains("Continue", true) ->
                Log.d(TAG, "Decision: Click Continue")

            screenInfo.contains("OK", true) ->
                Log.d(TAG, "Decision: Click OK")

            screenInfo.contains("Login", true) ->
                Log.d(TAG, "Decision: Wait for credentials")

            else ->
                Log.d(TAG, "Decision: No Action")
        }

        Log.d(TAG, "===========================")
    }
}
