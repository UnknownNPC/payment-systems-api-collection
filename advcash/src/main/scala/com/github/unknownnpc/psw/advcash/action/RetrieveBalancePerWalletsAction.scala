package com.github.unknownnpc.psw.advcash.action

import com.github.unknownnpc.psw.advcash.model.{BalancesResponse, AuthInfo}
import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[advcash] trait RetrieveBalancePerWalletsAction extends
  ActionContext[AuthInfo, BalancesResponse, HttpPost, String] with RestHttpExecutor[HttpPost]

object RetrieveBalancePerWalletsAction {

  def apply(httpClientParam: CloseableHttpClient): RetrieveBalancePerWalletsAction = new RetrieveBalancePerWalletsAction() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
