package com.github.aleksaqm.breakpointcounter.toolWindow

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser

@Service(Service.Level.PROJECT)
class MyToolWindowService(project: Project) {
    val browser = JBCefBrowser()

    fun updateBreakpoints(jsonData: String) {
//        thisLogger().warn("Executing JavaScript with data: $jsonData")
        browser.cefBrowser.executeJavaScript(
            "window.updateBreakpoints($jsonData);", browser.cefBrowser.url, 0
        )
    }

    companion object {
        fun getInstance(project: Project): MyToolWindowService =
            project.getService(MyToolWindowService::class.java)
    }
}