package com.github.unknownnpc.psw.api

object Utils {

  def safeParse[T](fn: => T): Either[ExternalAPIPayloadParseException, T] = {
    import scala.util.control.Exception.allCatch
    allCatch.either(fn).left.map(e => ExternalAPIPayloadParseException(cause = e))
  }

}
