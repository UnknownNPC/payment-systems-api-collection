package com.github.unknownnpc.psw.api

class APIException(message: String, cause: Throwable) extends Exception(message, cause)

final case
class InvalidParam(message: String = "Invalid external param", cause: Throwable)
  extends APIException(message, cause)

final case
class ExternalAPICallException(message: String = "External request error", cause: Throwable)
  extends APIException(message, cause)

final case
class APIParseException(message: String = "Unable to parse payload", cause: Throwable)
  extends APIException(message, cause)
