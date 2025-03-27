package com.github.aleksaqm.breakpointcounter.toolWindow

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandlerAdapter

@Service(Service.Level.PROJECT)
class MyToolWindowService {

    private val browser = JBCefBrowser("http://localhost:5173")
    @Volatile
    private var lastLoadedUrl: String? = "aleksa"

    init {
        browser.jbCefClient.addLoadHandler(object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(
                browser: CefBrowser?,
                frame: CefFrame?,
                httpStatusCode: Int
            ) {
                lastLoadedUrl = browser?.url
                thisLogger().warn("JCEF Page Loaded: $lastLoadedUrl")
            }
        }, browser.cefBrowser)
    }


    fun updateBreakpoints(jsonData: String) {
        val script = """window.updateBreakpoints($jsonData);"""

        if (!browser.isDisposed) {
            browser.cefBrowser.executeJavaScript(script, lastLoadedUrl, 0)
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