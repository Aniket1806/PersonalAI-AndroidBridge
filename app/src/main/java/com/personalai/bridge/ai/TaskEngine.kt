package com.personalai.bridge.ai

import android.util.Log

object TaskEngine {

    private const val TAG = "TaskEngine"

    private var currentGoal: String? = null
    private val steps = mutableListOf<String>()
    private var currentStep = 0

    fun startGoal(goal: String, plan: List<String>) {
        currentGoal = goal
        steps.clear()
        steps.addAll(plan)
        currentStep = 0

        Log.d(TAG, "Goal Started: $goal")
    }

    fun getCurrentStep(): String? {
        return if (currentStep < steps.size) {
            steps[currentStep]
        } else {
            null
        }
    }

    fun completeStep() {
        if (currentStep < steps.size) {
            Log.d(TAG, "Completed: ${steps[currentStep]}")
            currentStep++
        }

        if (currentStep >= steps.size) {
            Log.d(TAG, "Goal Completed: $currentGoal")
            currentGoal = null
            steps.clear()
            currentStep = 0
        }
    }

    fun hasGoal(): Boolean {
        return currentGoal != null
    }

    fun getGoal(): String? {
        return currentGoal
    }

    fun cancelGoal() {
        Log.d(TAG, "Goal Cancelled")

        currentGoal = null
        steps.clear()
        currentStep = 0
    }
}
