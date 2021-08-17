package com.example.sixth.util


open class CommonException(message: String = "") : Exception(message)

class ContentEmptyException(message: String = "") : CommonException(message)
class CannotReachServerException(message: String = "") : CommonException(message)
class NotAuthorizedException(message: String = "") : CommonException(message)
class NotConnectedException(message: String = "") : CommonException(message)
class ParseException(message: String = "") : CommonException(message)
class ServerException(message: String = "") : CommonException(message)

fun Throwable?.optMessage(): String {
    return if (this is NotConnectedException) {
        this.message.takeIf { !it.isNullOrEmpty() }
            ?: "The app needs an internet connection. Please check your connection."
    } else {
        this?.message.takeIf { !it.isNullOrEmpty() }
            ?: "Something went wrong..."
    }
}

internal class SixtApiException(message: String?) : CommonException(
    message ?: "Unknown Error..."
)