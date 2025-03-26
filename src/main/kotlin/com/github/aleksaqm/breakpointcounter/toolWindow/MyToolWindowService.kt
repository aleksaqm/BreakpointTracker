package com.github.aleksaqm.breakpointcounter.toolWindow

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandlerAdapter
import java.util.*

@Service(Service.Level.PROJECT)
class MyToolWindowService(project: Project) {

    private val browser = JBCefBrowser("http://localhost:5173")

    init {
        browser.jbCefClient.addLoadHandler(object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(
                browser: CefBrowser?,
                frame: CefFrame?,
                httpStatusCode: Int
            ) {
                thisLogger().warn("✅ JCEF Page Loaded: ${browser?.url}")
            }
        }, browser.cefBrowser)
    }

    fun updateBreakpoints(jsonData: String) {
        val script = """window.updateBreakpoints($jsonData);"""

        if (!browser.isDisposed) {
            val currentUrl = browser.cefBrowser.url
            if (currentUrl.isNullOrBlank()) {
                thisLogger().warn("⚠️ JCEF page is not loaded yet. Retrying in 1s...")
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        updateBreakpoints(jsonData) // Retry after delay
                    }
                }, 1000)
                return
            }

            thisLogger().warn("🚀 Executing JavaScript on: $currentUrl")
            browser.cefBrowser.executeJavaScript(script, browser.cefBrowser.url, 0)
        }
    }

//    fun updateBreakpoints(jsonData: String) {
//        val script = """window.updateBreakpoints($jsonData);"""
//
//        if (!browser.isDisposed) {
////            thisLogger().warn("QQMMMMMMMMMMM Executing JavaScript: window.updateBreakpoints($jsonData);")
//            thisLogger().warn("QQMMMMMMMMMMM Executing JavaScript on: ${browser.cefBrowser.url}")
//            browser.cefBrowser.executeJavaScript(script, browser.cefBrowser.url, 0)
//        }
//    }

    companion object {
        fun getInstance(project: Project): MyToolWindowService =
            project.getService(MyToolWindowService::class.java)
    }

    fun getBrowser(): JBCefBrowser {
        return browser
    }
}