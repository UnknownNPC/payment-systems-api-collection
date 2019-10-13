package com.github.unknownnpc.psw.qiwi

import java.util.Optional

import com.github.unknownnpc.psw.api.APIException
import com.github.unknownnpc.psw.qiwi.action.{RetrieveAccountBalanceAction, RetrieveTransferHistoryAction}
import com.github.unknownnpc.psw.qiwi.model._
import com.github.unknownnpc.psw.qiwi.serializer.QiwiSerializer._
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

import scala.collection.JavaConverters._

private[qiwi] class QiwiAPI(token: String,
                            retrieveTransferHistoryAction: RetrieveTransferHistoryAction,
                            retrieveAccountBalanceAction: RetrieveAccountBalanceAction) {

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
                              sources: List[ReqSources.Value] = List.empty, startEndDates: Option[WalletHistory#StartEndDates] = None,
                              nextPage: Option[WalletHistory#NextPage] = None): Either[APIException, WalletHistoryResponse] = {
    retrieveTransferHistoryAction.run(
      WalletHistoryRequest(token, personId, rows, operation, sources, startEndDates, nextPage)
    )
  }

  def retrieveTransferHistoryJava(personId: String, rows: Optional[Integer],
                                  operation: Optional[ReqTransferType.Value],
                                  sources: java.util.List[ReqSources.Value],
                                  startEndDates: Optional[WalletHistory#StartEndDates],
                                  nextPage: Optional[WalletHistory#NextPage]): Either[APIException, WalletHistoryResponse] = {
    retrieveTransferHistory(personId,
      rows.orElse(10),
      Option(operation.orElse(ReqTransferType.ALL)),
      sources.asScala.toList,
      Option(startEndDates.orElse(null)),
      Option(nextPage.orElse(null))
    )
  }

  /**
    * Requests wallets balance
    * https://developer.qiwi.com/ru/qiwi-wallet-personal/#balances_list
    *
    * @param personId the personId value, eg: 30501234567
    * @return the entity with response or error
    */
  def retrieveAccountBalance(personId: String): Either[APIException, AccountBalanceResponse] = {
    retrieveAccountBalanceAction.run(
      AccountBalanceRequest(token, personId)
    )
  }

  def retrieveAccountBalanceJava(personId: String): Either[APIException, AccountBalanceResponse] = {
    retrieveAccountBalance(personId)
  }

}

object QiwiAPI {

  def apply(token: String, httpClient: CloseableHttpClient = HttpClients.createDefault()): QiwiAPI = {

    val retrieveTransferHistoryAction = RetrieveTransferHistoryAction(httpClient)
    val retrieveAccountBalanceAction = RetrieveAccountBalanceAction(httpClient)

    new QiwiAPI(token, retrieveTransferHistoryAction, retrieveAccountBalanceAction)
  }

  def getInstance(token: String) = apply(token)

  def getInstance(token: String, httpClient: CloseableHttpClient) = apply(token, httpClient)

}
