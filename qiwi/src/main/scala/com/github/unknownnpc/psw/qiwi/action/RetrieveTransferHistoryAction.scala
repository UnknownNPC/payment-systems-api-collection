package com.github.unknownnpc.psw.qiwi.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import com.github.unknownnpc.psw.qiwi.model.{WalletHistoryRequest, WalletHistoryResponse}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient

private[qiwi] trait RetrieveTransferHistoryAction extends
  ActionContext[WalletHistoryRequest, WalletHistoryResponse, HttpGet, String] with RestHttpExecutor[HttpGet]

object RetrieveTransferHistoryAction {

  def apply(httpClientParam: CloseableHttpClient): RetrieveTransferHistoryAction = new RetrieveTransferHistoryAction() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}