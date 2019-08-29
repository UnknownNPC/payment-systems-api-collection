package com.github.unknownnpc.psw.api

trait Serializer[IN, OUT, REQ, RES] {

  def toReq(obj: IN): Either[APIParseException, REQ]

  def fromRes(out: RES): Either[APIParseException, OUT]

}
