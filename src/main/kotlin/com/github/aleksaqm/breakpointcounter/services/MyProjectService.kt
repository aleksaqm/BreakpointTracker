package com.github.aleksaqm.breakpointcounter.services

import com.github.aleksaqm.breakpointcounter.listeners.BreakpointListener
import com.github.aleksaqm.breakpointcounter.toolWindow.MyToolWindowService
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import java.io.File
import java.io.IOException

class MyProjectService(private val project: Project) : Disposable {

    private var connection: MessageBusConnection? = null

    init {
        subscribeToBreakpointEvents()
        startFrontendServer()
    }

    private fun subscribeToBreakpointEvents() {
        connection = project.messageBus.connect()

        val browser = MyToolWindowService.getInstance(project).getBrowser()


        val listener = BreakpointListener(project, browser)

        connection?.subscribe(XBreakpointListener.TOPIC, object : XBreakpointListener<XBreakpoint<*>> {
            override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
                listener.breakpointAdded(breakpoint)
            }

            override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
                listener.breakpointRemoved(breakpoint)
            }

            override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
                listener.breakpointChanged(breakpoint)
            }
        })
    }

    override fun dispose() {
        connection?.disconnect()
    }

    private fun startFrontendServer() {
        val frontendPath = File(System.getProperty("user.dir"), "frontend")
        thisLogger().warn("AAAAAAAAAAAAAAAAAAAAAAAAA $frontendPath")
        val processBuilder = ProcessBuilder()
            .command("npm", "run", "dev")
            .directory(File(frontendPath.toString()))
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)

        try {
            processBuilder.start()
            thisLogger().warn("Frontend server started successfully.")
        } catch (e: IOException) {
            thisLogger().error("Failed to start frontend server.", e)
        }
    }

}
