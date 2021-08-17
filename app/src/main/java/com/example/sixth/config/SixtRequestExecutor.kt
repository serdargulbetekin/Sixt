package com.example.sixth.config

import android.content.Context
import com.example.sixth.constant.Constant
import com.example.sixth.util.SixtApiException
import io.reactivex.Single
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject

class SixtRequestExecutor(
    context: Context,
    client: OkHttpClient,
    requestCreator: RequestCreator,
) : RequestExecutor(context, client, requestCreator) {

    fun <T> singleApi(
        endPoint: String,
        postParams: Map<String, Any>? = null,
        isPostParamsJson: Boolean = false,
        parser: (String) -> T
    ): Single<T> {
        val jsonParams = if (isPostParamsJson) {
            val jsonObject = JSONObject()
            postParams?.forEach { entry ->
                jsonObject.put(entry.key, entry.value)
            }
            jsonObject
        } else {
            null
        }

        return single(url = getApiUrl() + endPoint,
            headers = getHeaders(),
            formData = if (!isPostParamsJson) postParams?.mapValues { it.value.toString() } else null,
            body = if (isPostParamsJson) jsonParams?.toString() else null,
            parser = { result ->
                val jsonArray = JSONArray(result)
                val isSuccess = jsonArray.length()>0
                val errorDescription = "Technical issue found...."
                if (!isSuccess) {
                    throw SixtApiException(message = errorDescription)
                } else {
                    parser(result)
                }
            })


    }

    private fun getHeaders(): Map<String, String> {
        return mutableMapOf<String, String>()
    }

    private fun getApiUrl(): String {
        return Constant.BASE_URL
    }

}

