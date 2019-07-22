package com.github.unknownnpc.psw.api

class APIException(message: String, cause: Throwable) extends Exception(message, cause)

final case
class InvalidRequestException(message: String = "Invalid request exception", cause: Throwable)
  extends APIException(message, cause)

final case
class TimeoutException(message: String = "Request timeout exception", cause: Throwable)
  extends APIException(message, cause)

final case
class InvalidResponse(message: String = "Invalid response exception", cause: Throwable)
  extends APIException(message, cause)
