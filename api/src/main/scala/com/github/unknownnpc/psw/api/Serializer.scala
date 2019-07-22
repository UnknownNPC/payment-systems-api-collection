package com.github.unknownnpc.psw.api

trait Serializer[IN, OUT, REQ, RES] {

  def toReq(obj: IN): REQ

  def fromRes(out: RES): OUT

}
