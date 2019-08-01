package com.github.unknownnpc.psw.api.action

import com.github.unknownnpc.psw.api.executor.Executor
import com.github.unknownnpc.psw.api.{APIException, ExternalAPIException, Serializer}

import scala.util.Try

trait ActionContext[IN, OUT, REQ, RES] {
  executor: Executor[REQ, RES] =>

  def run(in: IN)(implicit ser: Serializer[IN, OUT, REQ, RES]): Either[APIException, OUT] = {
    Try(ser.fromRes(
      executor.execute(
        ser.toReq(in)
      )
    )).toEither.left
      .map(e => ExternalAPIException(cause = e))
  }

}
