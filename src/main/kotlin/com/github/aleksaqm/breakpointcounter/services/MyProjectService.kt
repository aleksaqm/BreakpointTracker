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

class MyProjectService(private val project: Project) : Disposable {

    private var connection: MessageBusConnection? = null

    init {
//        thisLogger().warn("MyProjectService initialized for project: ${project.name}")
        subscribeToBreakpointEvents()
    }

    private fun subscribeToBreakpointEvents() {
        connection = project.messageBus.connect()

        // Get the JCEF browser instance from MyToolWindowService
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

    fun getRandomNumber() = (1..100).random()
}
