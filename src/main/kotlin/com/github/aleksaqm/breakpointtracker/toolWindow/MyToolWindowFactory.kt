package com.github.aleksaqm.breakpointtracker.toolWindow

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.github.aleksaqm.breakpointtracker.services.MyProjectService


class MyToolWindowFactory : ToolWindowFactory {

    private lateinit var myProjectService: MyProjectService

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        myProjectService = project.getService(MyProjectService::class.java)
        val toolWindowService = MyToolWindowService.getInstance(project)
        val browser = toolWindowService.getBrowser()

        val content = ContentFactory.getInstance().createContent(browser.component, "", false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true
}
