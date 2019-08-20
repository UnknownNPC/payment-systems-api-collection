package com.github.unknownnpc.psw.qiwi.model

import java.util.Date

object QiwiModel {

  object WalletHistory {

    type StartEndDates = (Date, Date)
    type NextPage = (Date, Long)

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

    case class Request(apiToken: String, personId: String, rows: Int = 10,
                       operation: Option[ReqTransferType.Value] = Some(ReqTransferType.ALL),
                       sources: List[ReqSources.Value] = List.empty, startEndDates: Option[StartEndDates] = None,
                       nextPage: Option[NextPage] = None)

    case class Response(data: List[ResponseData], nextTxnId: Option[Long], nextTxnDate: Option[Date])
    case class ResponseData(txnId: Long, personId: Long, date: Date, errorCode: Long, error: String, status: ResStatus.Value,
                            statusText: String, trmTxnId: String, account: String, sum: ResponseAmountCurrency, total: ResponseAmountCurrency,
                            provider: ResponseProvider, comment: String, currencyRate: BigDecimal, chequeReady: Option[Boolean],
                            bankDocumentAvailable: Option[Boolean], bankDocumentReady: Option[Boolean], repeatPaymentEnabled: Option[Boolean],
                            favoritePaymentEnabled: Option[Boolean], regularPaymentEnabled: Option[Boolean])
    case class ResponseAmountCurrency(amount: BigDecimal, currency: String)
    case class ResponseProvider(id: Long, shortName: String, longName: String, logoUrl: String, description: String,
                                keys: String, siteUrl: String)

  }

  object AccountBalance {

    case class Request(apiToken: String, wallet: String)

    case class Response(accounts: List[ResponseAccount])
    case class ResponseAccount(alias: String, fsAlias: String, bankAlias: String,
                               title: String, `type`: ResponseAccountType, hasBalance: Boolean, balance: ResponseAccountBalance,
                               currency: Int, defaultAccount: Boolean)
    case class ResponseAccountType(id: String, title: String)
    case class ResponseAccountBalance(amount: BigDecimal, currency: Int)

  }

}
