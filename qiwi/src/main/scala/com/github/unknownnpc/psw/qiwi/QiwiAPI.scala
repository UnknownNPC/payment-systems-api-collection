package com.github.unknownnpc.psw.qiwi

import com.github.unknownnpc.psw.qiwi.action.RetrieveTransferHistoryAction
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.WalletHistory
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.WalletHistory.{NextPage, ReqSources, ReqTransferType, StartEndDates}
import com.github.unknownnpc.psw.qiwi.serializer.QiwiSerializer._
import org.apache.http.impl.client.CloseableHttpClient

private[qiwi] class QiwiAPI(token: String, httpClient: CloseableHttpClient) {

  def retrieveTransferHistory(wallet: String, rows: Int = 10,
                              operation: Option[ReqTransferType.Value] = Some(ReqTransferType.ALL),
                              sources: List[ReqSources.Value] = List.empty, startEndDates: Option[StartEndDates] = None,
                              nextPage: Option[NextPage] = None): WalletHistory.Response = {
    RetrieveTransferHistoryAction(httpClient).run(
      WalletHistory.Request(token, wallet, rows, operation, sources, startEndDates, nextPage)
    )
  }

}

object QiwiAPI {

  def apply(token: String, httpClient: CloseableHttpClient): QiwiAPI = new QiwiAPI(token, httpClient)

}
