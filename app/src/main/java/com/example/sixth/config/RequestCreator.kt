package com.example.sixth.config


import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.net.MalformedURLException

class RequestCreator {

    fun createRequest(url: String?, headers: Headers?, body: String?, formData: Map<String, String>?): Request {
        if (url.isNullOrEmpty()) throw MalformedURLException()
        val builder = Request.Builder()
        if (headers != null) {
            builder.headers(headers)
        }
        builder.url(url)
        if (formData != null) {
            builder.post(FormBody.Builder().apply {
                formData.forEach {
                    addEncoded(it.key, it.value)
                }
            }
                .build()
            )
        } else {
            if (body.isNullOrEmpty()) {
                builder.get()
            } else {
                builder.post(RequestBody.create("application/json".toMediaTypeOrNull(),
                    body
                ))
            }
        }
        return builder.build()
    }
}