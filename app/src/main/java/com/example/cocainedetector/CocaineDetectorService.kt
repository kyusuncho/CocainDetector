package com.example.cocainedetector

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.cocainedetector.data.Detection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CocaineDetectorService : AccessibilityService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var lastCheckTime: Long = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val now = System.currentTimeMillis()
        if (now - lastCheckTime < 2000L) {
            return
        }
        lastCheckTime = now

        val rootNode = rootInActiveWindow ?: return
        // Try to refresh the root node to get the latest content if possible
        rootNode.refresh()
        val screenText = collectScreenText(rootNode)

        if (screenText.lowercase().contains("cocaine")) {
            val packageName = event?.packageName?.toString() ?: "unknown"
            handleDetection(packageName, screenText)
        }
    }

    private fun collectScreenText(node: AccessibilityNodeInfo): String {
        val builder = StringBuilder()

        fun traverse(n: AccessibilityNodeInfo) {
            val text = n.text?.toString()
            if (!text.isNullOrBlank()) {
                builder.append(text).append('\n')
            }
            for (i in 0 until n.childCount) {
                val child = n.getChild(i)
                if (child != null) {
                    traverse(child)
                    child.recycle()
                }
            }
        }

        traverse(node)
        return builder.toString().trim()
    }

    private fun handleDetection(packageName: String, screenText: String) {
        scope.launch {
            val detection = Detection(
                timestamp = System.currentTimeMillis(),
                packageName = packageName,
                screenText = screenText,
                detectedText = "cocaine"
            )
            
            val db = (application as CocaineDetectorApplication).database
            db.detectionDao().insert(detection)
            Log.d("CocaineDetector", "Detection saved: $detection")
        }
    }

    override fun onInterrupt() {
        // Service interrupted
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
