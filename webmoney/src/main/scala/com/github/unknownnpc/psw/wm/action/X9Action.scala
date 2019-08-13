package com.github.unknownnpc.psw.wm.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import com.github.unknownnpc.psw.wm.model.Model.X9
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[action] trait X9Action extends
  ActionContext[X9.Request, X9.Response, HttpPost, String] with RestHttpExecutor[HttpPost] {
}

object X9Action {

  def apply(httpClientParam: CloseableHttpClient): X9Action = new X9Action() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
