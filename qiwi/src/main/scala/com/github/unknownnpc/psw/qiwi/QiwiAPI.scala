package com.github.unknownnpc.psw.qiwi

import com.github.unknownnpc.psw.api.APIException
import com.github.unknownnpc.psw.qiwi.action.{RetrieveAccountBalanceAction, RetrieveTransferHistoryAction}
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.WalletHistory.{NextPage, ReqSources, ReqTransferType, StartEndDates}
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.{AccountBalance, WalletHistory}
import com.github.unknownnpc.psw.qiwi.serializer.QiwiSerializer._
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

private[qiwi] class QiwiAPI(token: String, httpClient: CloseableHttpClient) {

  /**
    * Warn: API limit is ltq 100 requests per minute, otherwise client ban for 5 minunutes
    * https://developer.qiwi.com/ru/qiwi-wallet-personal/#payments_list
    *
    * @param wallet        the qiwi personId, without `+` char, eg: 30501234567
    * @param rows          the number of transactions, max is 50
    * @param operation     the operation type
    * @param sources       the sources
    * @param startEndDates the start/end date
    * @param nextPage      the next page param
    * @return the entity with response or error
    */
  def retrieveTransferHistory(personId: String, rows: Int = 10,
                              operation: Option[ReqTransferType.Value] = Some(ReqTransferType.ALL),
                              sources: List[ReqSources.Value] = List.empty, startEndDates: Option[StartEndDates] = None,
                              nextPage: Option[NextPage] = None): Either[APIException, WalletHistory.Response] = {
    RetrieveTransferHistoryAction(httpClient).run(
      WalletHistory.Request(token, personId, rows, operation, sources, startEndDates, nextPage)
    )
  }

  /**
    * Requests wallets balance
    * https://developer.qiwi.com/ru/qiwi-wallet-personal/#balances_list
    *
    * @param personId the personId valie, eg: 30501234567
    * @return the entity with response or error
    */
  def retrieveAccountBalance(personId: String): Either[APIException, AccountBalance.Response] = {
    RetrieveAccountBalanceAction(httpClient).run(
      AccountBalance.Request(token, personId)
    )
  }

}

object QiwiAPI {

  def apply(token: String, httpClient: CloseableHttpClient = HttpClients.createDefault()): QiwiAPI =
    new QiwiAPI(token, httpClient)

  def getInstance(token: String) = apply(token)

}
