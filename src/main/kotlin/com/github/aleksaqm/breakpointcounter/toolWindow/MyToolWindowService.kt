package com.github.aleksaqm.breakpointcounter.toolWindow

import com.github.aleksaqm.breakpointcounter.services.FrontendServer
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandlerAdapter

@Service(Service.Level.PROJECT)
class MyToolWindowService {

    private val frontendServer = FrontendServer()
    private val browser: JBCefBrowser
    @Volatile
    private var lastLoadedUrl: String? = null // Start as null

    init {
        frontendServer.start() // Start the frontend server
        val frontendUrl = "http://localhost:${frontendServer.listeningPort}/index.html"
        thisLogger().warn("AAAAAAAAAAAAAAAAAAAAAAAAAAA Frontend running at: $frontendUrl")

        browser = JBCefBrowser(frontendUrl)
        thisLogger().warn("🚀 JCEF Browser initialized with URL: $frontendUrl")

        browser.jbCefClient.addLoadHandler(object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(
                browser: CefBrowser?,
                frame: CefFrame?,
                httpStatusCode: Int
            ) {
                lastLoadedUrl = browser?.url ?: frontendUrl // Ensure it never stays null
                thisLogger().warn("🔄 JCEF Page Loaded: $lastLoadedUrl")
            }
        }, browser.cefBrowser)
    }

    fun updateBreakpoints(jsonData: String) {
        val script = """window.updateBreakpoints($jsonData);"""

        if (!browser.isDisposed) {
            val urlToUse = lastLoadedUrl
            browser.cefBrowser.executeJavaScript(script, urlToUse, 0)
        }
    }

    companion object {
        fun getInstance(project: Project): MyToolWindowService =
            project.getService(MyToolWindowService::class.java)
    }

    fun getBrowser(): JBCefBrowser {
        return browser
    }
}