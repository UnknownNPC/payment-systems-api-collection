package com.github.unknownnpc.psw.api

object Utils {

  def safeParse[T](fn: => T): Either[APIParseException, T] = {
    import scala.util.control.Exception.allCatch
    allCatch.either(fn).left.map(e => APIParseException(cause = e))
  }

  def unPrettyOut(string: String): String = {
    string.replaceAll(">\\s+<", "><")
  }

}
