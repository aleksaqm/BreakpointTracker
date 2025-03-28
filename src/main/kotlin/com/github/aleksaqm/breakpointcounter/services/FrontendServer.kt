package com.github.aleksaqm.breakpointcounter.services

import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.InputStream
import java.net.ServerSocket

class FrontendServer : NanoHTTPD(findFreePort()) {
    private val devFrontendPath = File("frontend/dist")

    companion object {
        fun findFreePort(): Int {
            ServerSocket(0).use { socket ->
                return socket.localPort
            }
        }
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.removePrefix("/")
        val resourcePath = "/frontend/dist/$uri"

        val devFile = File(devFrontendPath, uri)
        if (devFile.exists() && devFile.isFile) {
            return newChunkedResponse(Response.Status.OK, getMimeType(uri), devFile.inputStream())
        }

        val inputStream: InputStream? = this::class.java.getResourceAsStream(resourcePath)
        return if (inputStream != null) {
            newChunkedResponse(Response.Status.OK, getMimeType(uri), inputStream)
        } else {
            newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found")
        }
    }

    private fun getMimeType(uri: String): String {
        return when {
            uri.endsWith(".html") -> "text/html"
            uri.endsWith(".css") -> "text/css"
            uri.endsWith(".js") -> "application/javascript"
            uri.endsWith(".png") -> "image/png"
            uri.endsWith(".jpg") || uri.endsWith(".jpeg") -> "image/jpeg"
            else -> "text/plain"
        }
    }
}