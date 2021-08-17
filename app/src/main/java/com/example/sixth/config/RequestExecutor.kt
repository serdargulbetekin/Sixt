package com.example.sixth.config

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import com.example.sixth.util.*
import io.reactivex.Single
import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Response

open class RequestExecutor(
    private val context: Context,
    private val client: OkHttpClient,
    private val requestCreator: RequestCreator
) {

    private val TAG = "REQUEST_EXECUTOR"

    private fun <T> sync(
        url: String?,
        headers: Headers?,
        body: String? = null,
        formData: Map<String, String>? = null,
        parser: ((content: String) -> T)
    ): T? {

        if (!isInternetAvailablePre(context)) {
            throw NotConnectedException()
        }
        val response: Response?
        try {
            response =
                client.newCall(requestCreator.createRequest(url, headers, body, formData)).execute()
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "")
            throw checkInternetConnection(context)
        }
        when (response.code) {
            200, 201 -> {
                val responseString = response.body?.string()
                if (responseString == null || responseString.isEmpty()) {
                    throw ContentEmptyException()
                }
                try {
                    return parser.invoke(responseString)
                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "")
                    throw e as? CommonException
                        ?: ParseException()
                }
            }
            204 -> {
                throw ContentEmptyException()
            }
            401 -> {
                throw NotAuthorizedException("Yetkisiz kullanım. Lütfen daha sonra tekrar deneyin.")
            }
            else -> {
                throw CannotReachServerException("Sunucuya ulaşılamadı.")
            }
        }
    }

    fun <T> single(
        url: String,
        headers: Map<String, String>? = null,
        body: String? = null,
        formData: Map<String, String>? = null,
        parser: ((content: String) -> T)
    ): Single<T> {
        return Single.create { emitter ->
            try {
                val headerItems = headers?.toHeaders()
                val response = sync(url, headerItems, body, formData, parser)
                if (response == null) {
                    emitter.onError(ServerException(""))
                } else {
                    emitter.onSuccess(response)
                }
            } catch (e: Exception) {
                if (e !is InterruptedException) {
                    emitter.onError(e)
                }
            }
        }
    }

    private fun checkInternetConnection(context: Context): Throwable {
        return if (isInternetAvailable(context)) {
            CannotReachServerException()
        } else {
            NotConnectedException()
        }
    }

    private fun isInternetAvailablePre(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= 21) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return !(connectivityManager.activeNetworkInfo == null || connectivityManager.activeNetworkInfo?.isConnected == false)
        } else {
            return true
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        return if (isInternetAvailablePre(context)) {
            false
        } else {
            try {
                Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
                false
            }
        }
    }
}