package com.github.aleksaqm.breakpointcounter.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.aleksaqm.breakpointcounter.MyBundle
import com.github.aleksaqm.breakpointcounter.services.MyProjectService
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.util.ui.JBUI
import javax.swing.JButton
import javax.swing.JPanel


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myProjectService = project.getService(MyProjectService::class.java)
        val toolWindowService = MyToolWindowService.getInstance(project)
        val browser = toolWindowService.browser

        // Load basic HTML structure
        browser.loadHTML("""
                <html>
                <head>
                    <script>
                        window.updateBreakpoints = function (breakpoints) {
                            document.getElementById('count').innerText = 
                                "Total Breakpoints: " + (breakpoints.length-1);
                            document.getElementById('content').innerHTML = 
                                breakpoints.map(bp => `<p>$${"$"}{bp.file}</p>`).join('');
                        };
                    </script>
                </head>
                <body>
                    <h2>Breakpoint Tracker</h2>
                    <p id="count">Total Breakpoints: 0</p>
                    <div id="content">No breakpoints</div>
                </body>
                </html>
            """)

        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(JPanel().apply { add(browser.component) }, "", false)
        contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val label = JBLabel(MyBundle.message("randomLabel", "?"))

            add(label)
            add(JButton(MyBundle.message("shuffle")).apply {
                addActionListener {
                    label.text = MyBundle.message("randomLabel", service.getRandomNumber())
                }
            })
        }
    }
}
