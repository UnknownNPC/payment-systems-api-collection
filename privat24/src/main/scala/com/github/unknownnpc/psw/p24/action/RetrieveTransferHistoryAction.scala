package com.github.unknownnpc.psw.p24.action

import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.PostRestHttpExecutor
import com.github.unknownnpc.psw.p24.model.P24Model.WalletHistory.{WalletHistoryRequest, WalletHistoryResponse}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[action] trait RetrieveTransferHistoryAction extends
  ActionContext[WalletHistoryRequest, WalletHistoryResponse, HttpPost, String] with PostRestHttpExecutor

object RetrieveTransferHistoryAction {

  def apply(httpClient: CloseableHttpClient): RetrieveTransferHistoryAction = new RetrieveTransferHistoryAction() {
    override val httpClient: CloseableHttpClient = httpClient
  }

}

