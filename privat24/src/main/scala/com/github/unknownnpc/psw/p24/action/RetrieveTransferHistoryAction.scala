package com.github.unknownnpc.psw.p24.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import com.github.unknownnpc.psw.p24.model.{Request, WalletHistoryResponse}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[p24] trait RetrieveTransferHistoryAction extends
  ActionContext[Request, WalletHistoryResponse, HttpPost, String] with RestHttpExecutor[HttpPost]

object RetrieveTransferHistoryAction {

  def apply(httpClientParam: CloseableHttpClient): RetrieveTransferHistoryAction = new RetrieveTransferHistoryAction() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
