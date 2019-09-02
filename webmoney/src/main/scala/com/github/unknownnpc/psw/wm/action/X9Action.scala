package com.github.unknownnpc.psw.wm.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import com.github.unknownnpc.psw.wm.model.{X9Request, X9Response}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[wm] trait X9Action extends
  ActionContext[X9Request, X9Response, HttpPost, String] with RestHttpExecutor[HttpPost]

object X9Action {

  def apply(httpClientParam: CloseableHttpClient): X9Action = new X9Action() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
