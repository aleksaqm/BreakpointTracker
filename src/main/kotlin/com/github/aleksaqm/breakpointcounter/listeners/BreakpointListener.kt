package com.github.aleksaqm.breakpointcounter.listeners

import com.github.aleksaqm.breakpointcounter.toolWindow.MyToolWindowService
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class BreakpointInfo(val file: String, val line: Int?)

class BreakpointListener(private val project: Project, private val browser: JBCefBrowser) :
    XBreakpointListener<XBreakpoint<*>> {

    init {
        updateBreakpoints()
    }

    override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
        updateBreakpoints()
    }

    override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
        updateBreakpoints()
    }

    override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
        updateBreakpoints()
    }

    private fun updateBreakpoints() {
        val breakpointManager = XDebuggerManager.getInstance(project).breakpointManager
        val breakpoints = breakpointManager.allBreakpoints

        val projectBasePath = project.basePath?.replace("\\", "/") ?: ""

        val breakpointData = breakpoints.mapNotNull { bp ->
            bp.sourcePosition?.file?.path?.replace("\\", "/")?.let { filePath ->
                val relativePath = filePath.removePrefix("$projectBasePath/")
                BreakpointInfo(relativePath, bp.sourcePosition?.line?.plus(1))
            }
        }

        val jsonData = Json.encodeToString(breakpointData)
        val toolWindowService = MyToolWindowService.getInstance(project)
        toolWindowService.updateBreakpoints(jsonData)
    }
}