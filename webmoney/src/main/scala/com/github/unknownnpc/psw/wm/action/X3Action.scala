package com.github.unknownnpc.psw.wm.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import com.github.unknownnpc.psw.wm.model.{X3Request, X3Response}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[wm] trait X3Action extends
  ActionContext[X3Request, X3Response, HttpPost, String] with RestHttpExecutor[HttpPost]

object X3Action {

  def apply(httpClientParam: CloseableHttpClient): X3Action = new X3Action() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
