package com.github.unknownnpc.psw.advcash.action

import com.github.unknownnpc.psw.advcash.model.{TransactionHistoryRequest, TransactionHistoryResponse}
import com.github.unknownnpc.psw.api.action.ActionContext
import com.github.unknownnpc.psw.api.executor.RestHttpExecutor
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient

private[advcash] trait RetrieveTransactionHistoryAction extends
  ActionContext[TransactionHistoryRequest, TransactionHistoryResponse, HttpPost, String] with RestHttpExecutor[HttpPost]

object RetrieveTransactionHistoryAction {

  def apply(httpClientParam: CloseableHttpClient): RetrieveTransactionHistoryAction = new RetrieveTransactionHistoryAction() {
    override val httpClient: CloseableHttpClient = httpClientParam
  }

}
