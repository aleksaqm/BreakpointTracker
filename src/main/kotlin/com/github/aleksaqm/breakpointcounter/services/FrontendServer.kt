package com.github.aleksaqm.breakpointcounter.services

import fi.iki.elonen.NanoHTTPD
import java.io.InputStream

class FrontendServer : NanoHTTPD(5173) {
    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.removePrefix("/")
        val resourcePath = "/frontend/dist/$uri"

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