package com.personalai.bridge.ai

import android.util.Log

object AIMemory {

    private const val TAG = "PersonalAIBridge"

    private val memory = mutableMapOf<String, String>()

    fun remember(screen: String, action: String) {
        memory[screen] = action
        Log.d(TAG, "Remembered -> $screen = $action")
    }

    fun recall(screen: String): String? {
        val result = memory[screen]
        Log.d(TAG, "Recall -> $screen = $result")
        return result
    }

    fun clear() {
        memory.clear()
        Log.d(TAG, "Memory Cleared")
    }
}
