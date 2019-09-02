package com.github.unknownnpc.psw.p24.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import com.github.unknownnpc.psw.p24.model.{CardBalanceResponse, Request}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[p24] trait RetrieveCardBalanceAction extends
  ActionContext[Request, CardBalanceResponse, HttpPost, String] with RestHttpExecutor[HttpPost]

object RetrieveCardBalanceAction {

  def apply(httpClientParam: CloseableHttpClient): RetrieveCardBalanceAction = new RetrieveCardBalanceAction() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
