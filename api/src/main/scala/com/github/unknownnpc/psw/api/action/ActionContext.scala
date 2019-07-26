package com.github.unknownnpc.psw.api.action

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.api.executor.Executor

trait ActionContext[IN, OUT, REQ, RES] {
  executor: Executor[REQ, RES] =>

  def run(in: IN)(implicit ser: Serializer[IN, OUT, REQ, RES]): OUT = {
    ser.fromRes(
      executor.execute(
        ser.toReq(in)
      )
    )
  }

}
