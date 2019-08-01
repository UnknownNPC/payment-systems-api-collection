package com.github.unknownnpc.psw.api

class APIException(message: String, cause: Throwable) extends Exception(message, cause)

final case
class InvalidParam(message: String = "Invalid external param", cause: Throwable)
  extends APIException(message, cause)

final case
class ExternalAPIException(message: String = "External request fail", cause: Throwable)
  extends APIException(message, cause)
