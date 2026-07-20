package com.personalai.bridge.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class BridgeAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Future: handle accessibility events here
    }

    override fun onInterrupt() {
        // Called when the service is interrupted
    }
}
