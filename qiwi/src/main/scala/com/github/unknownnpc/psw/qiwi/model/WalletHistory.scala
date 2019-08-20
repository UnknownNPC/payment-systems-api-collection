package com.github.unknownnpc.psw.qiwi.model

import java.util.Date

object ReqTransferType extends Enumeration {
  type ReqTransferType = Value
  val ALL, IN, OUT, QIWI_CARD = Value
}

/**
  * Should be as passed as serial number from 0, eg: MK eqs 4.
  */
object ReqSources extends Enumeration {
  type ReqSources = Value
  val QW_RUB, QW_USD, QW_EUR, CARD, MK = Value
}

object ResStatus extends Enumeration {
  type ResStatus = Value
  val WAITING, SUCCESS, ERROR = Value
}

trait WalletHistory {
  type StartEndDates = (Date, Date)
  type NextPage = (Date, Long)
}

case class WalletHistoryRequest(apiToken: String, personId: String, rows: Int = 10,
                                operation: Option[ReqTransferType.Value] = Some(ReqTransferType.ALL),
                                sources: List[ReqSources.Value] = List.empty, startEndDates: Option[WalletHistory#StartEndDates] = None,
                                nextPage: Option[WalletHistory#NextPage] = None)

case class WalletHistoryResponse(data: List[WalletHistoryResponseData], nextTxnId: Option[Long], nextTxnDate: Option[Date])

case class WalletHistoryResponseData(txnId: Long, personId: Long, date: Date, errorCode: Long, error: String, status: ResStatus.Value,
                                     statusText: String, trmTxnId: String, account: String, sum: WalletHistoryResponseAmountCurrency, total: WalletHistoryResponseAmountCurrency,
                                     provider: WalletHistoryResponseProvider, comment: String, currencyRate: BigDecimal, chequeReady: Option[Boolean],
                                     bankDocumentAvailable: Option[Boolean], bankDocumentReady: Option[Boolean], repeatPaymentEnabled: Option[Boolean],
                                     favoritePaymentEnabled: Option[Boolean], regularPaymentEnabled: Option[Boolean])

case class WalletHistoryResponseAmountCurrency(amount: BigDecimal, currency: String)

case class WalletHistoryResponseProvider(id: Long, shortName: String, longName: String, logoUrl: String, description: String,
                                         keys: String, siteUrl: String)
