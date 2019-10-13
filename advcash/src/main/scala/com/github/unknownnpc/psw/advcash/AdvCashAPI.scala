package com.github.unknownnpc.psw.advcash

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}
import java.util.{Date, Optional}

import com.github.unknownnpc.psw.advcash.action.{RetrieveBalancePerWalletsAction, RetrieveTransactionHistoryAction}
import com.github.unknownnpc.psw.advcash.model._
import com.github.unknownnpc.psw.advcash.serializer.AdvCashSerializer._
import com.github.unknownnpc.psw.api.APIException
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

private[advcash] class AdvCashAPI(apiName: String, apiPassword: String, apiEmail: String,
                                  retrieveBalancePerWalletsAction: RetrieveBalancePerWalletsAction,
                                  retrieveTransactionHistoryAction: RetrieveTransactionHistoryAction) {

  private val authZoneId = ZoneId.of("UTC")
  private val authTokenDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd:HH")

  /**
    * Get Balance per User’s Wallets.
    *
    * @return response with details or error
    */
  def retrieveBalancePerWallets: Either[APIException, BalancesResponse] = {
    val request = AuthInfo(apiName, getAuthenticationToken(), apiEmail)
    retrieveBalancePerWalletsAction.run(request)
  }

  def retrieveBalancePerWalletsJava(): Either[APIException, BalancesResponse] = {
    retrieveBalancePerWallets
  }

  /**
    * Get Transaction History.
    *
    * @param from              Ordinal number of transaction to start displaying with
    * @param count             The number of transactions for displaying
    * @param sortOrder         Sorting (takes values «ASC», «DESC», default «DESC»)
    * @param startTimeFrom     Start date for transactions to be selected
    * @param startTimeTo       End date for transactions to be selected
    * @param walletIdOpt       Wallet (optional parameter)
    * @param transactionName   Transaction name
    * @param transactionStatus Transaction status
    * @return the response with transactions or error
    */
  def retrieveTransactionsHistory(from: Int, count: Int, sortOrder: Sort.Value, startTimeFrom: Date, startTimeTo: Date, walletIdOpt: Option[String],
                                  transactionName: TransactionName.Value = TransactionName.ALL,
                                  transactionStatus: TransactionStatus.Value = TransactionStatus.COMPLETED): Either[APIException, TransactionHistoryResponse] = {
    val auth = AuthInfo(apiName, getAuthenticationToken(), apiEmail)
    val request = TransactionHistoryRequest(auth, from, count, sortOrder, startTimeFrom, startTimeTo, transactionName, transactionStatus, walletIdOpt)

    retrieveTransactionHistoryAction.run(request)
  }

  def retrieveTransactionsHistoryJava(from: Int, count: Int, sortOrder: Sort.Value, startTimeFrom: Date, startTimeTo: Date,
                                      walletIdOpt: Optional[String], transactionName: TransactionName.Value, transactionStatus: TransactionStatus.Value): Either[APIException, TransactionHistoryResponse] = {
    retrieveTransactionsHistory(
      from, count, sortOrder, startTimeFrom, startTimeTo, Option(walletIdOpt.orElse(null)), transactionName, transactionStatus
    )
  }

  private[advcash] def getAuthenticationToken(zonedate: ZonedDateTime = ZonedDateTime.now(authZoneId)): String = {

    import org.apache.commons.codec.digest.DigestUtils

    val dateFormat = zonedate.format(authTokenDateFormatter)
    val rawAuthenticationToken = apiPassword + ":" + dateFormat
    DigestUtils.sha256Hex(rawAuthenticationToken)
  }

}

object AdvCashAPI {

  def apply(apiName: String, apiPassword: String, apiEmail: String,
            httpClient: CloseableHttpClient = HttpClients.createDefault()): AdvCashAPI = {

    val retrieveBalancePerWalletsAction = RetrieveBalancePerWalletsAction(httpClient)
    val retrieveTransactionHistoryAction = RetrieveTransactionHistoryAction(httpClient)

    new AdvCashAPI(apiName, apiPassword, apiEmail, retrieveBalancePerWalletsAction, retrieveTransactionHistoryAction)
  }

  def getInstance(apiName: String, apiPassword: String, apiEmail: String) = apply(apiName, apiPassword, apiEmail)

}
