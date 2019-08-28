package com.github.unknownnpc.psw.api.action

import com.github.unknownnpc.psw.api.executor.Executor
import com.github.unknownnpc.psw.api.{APIException, ExternalAPICallException, Serializer}

import scala.util.control.Exception.allCatch

trait ActionContext[IN, OUT, REQ, RES] {
  executor: Executor[REQ, RES] =>

  def run(in: IN)(implicit ser: Serializer[IN, OUT, REQ, RES]): Either[APIException, OUT] = {

    for {
      request <- ser.toReq(in).right
      rawResponse <- allCatch.either(executor.execute(request))
        .left.map(e => ExternalAPICallException(cause = e)).right
      response <- ser.fromRes(rawResponse).right
    } yield response

  }

}
