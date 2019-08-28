package com.github.unknownnpc.psw.api

trait Serializer[IN, OUT, REQ, RES] {

  def toReq(obj: IN): Either[ExternalAPIPayloadParseException, REQ]

  def fromRes(out: RES): Either[ExternalAPIPayloadParseException, OUT]

}
