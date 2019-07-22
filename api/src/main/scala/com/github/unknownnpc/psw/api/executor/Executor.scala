package com.github.unknownnpc.psw.api.executor

private[api] trait Executor[Req, Res] {

  def execute(req: Req): Res

}
