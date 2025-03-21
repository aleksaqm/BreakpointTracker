package com.github.aleksaqm.breakpointcounter.listeners

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import com.intellij.xdebugger.breakpoints.XBreakpointManager
import com.intellij.openapi.project.Project
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BreakpointListener(private val project: Project, private val updateCallback: (String) -> Unit) :
    XBreakpointListener<XBreakpoint<*>> {

    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
//        thisLogger().warn("Breakpoint added: ${breakpoint.sourcePosition?.file?.path ?: "Unknown"}")
        updateBreakpoints()
    }

    override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
//        thisLogger().warn("Breakpoint removed: ${breakpoint.sourcePosition?.file?.path ?: "Unknown"}")
        updateBreakpoints()
    }

    override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
//        thisLogger().warn("Breakpoint changed: ${breakpoint.sourcePosition?.file?.path ?: "Unknown"}")
        updateBreakpoints()
    }

    private fun updateBreakpoints() {
        val breakpointManager: XBreakpointManager =
            XDebuggerManager.getInstance(project).breakpointManager
        val breakpoints = breakpointManager.allBreakpoints

        val breakpointData = breakpoints.mapNotNull { bp ->
            val filePath = bp.sourcePosition?.file?.path
            filePath?.let { mapOf("file" to it) }
        }

        val jsonData = Json.encodeToString(breakpointData)
        thisLogger().warn("Sending data to UI: $jsonData")
        updateCallback(jsonData)
    }
}