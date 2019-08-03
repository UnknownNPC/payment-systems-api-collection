package com.github.unknownnpc.psw.p24.model

import java.util.Date

private[p24] object P24Model {

  case class Merchant(id: Long, signature: Option[String] = None)

  case class Request(merchantPassword: String, merchant: Merchant, data: RequestData)
  case class RequestData(oper: String = "cmt", waitField: Long = 0, test: Long = 0, payment: RequestDataPayment)
  case class RequestDataPayment(idAttr: String = "", props: List[RequestDataProp])
  case class RequestDataProp(name: String, value: String)

  object WalletHistory {

    case class WalletHistoryResponse(merchant: Merchant, data: WalletHistoryResponseData)
    case class WalletHistoryResponseData(oper: String, info: WalletHistoryResponseInfo)
    case class WalletHistoryResponseInfo(status: String, credit: String, debet: String, statements: List[WalletHistoryResponseStatementEntity])
    case class WalletHistoryResponseStatementEntity(card: String, appcode: String, trandate: Date, amount: String, cardamount: String, rest: String, terminal: String, description: String)

  }

  object CardBalance {

    case class Response(merchant: Merchant, data: ResponseData)
    case class ResponseData(oper: String, cardBalance: ResponseCardBalance, card: ResponseCard)
    case class ResponseCardBalance(avBalance: BigDecimal, balDate: Date, balDyn: String,
                                   balance: BigDecimal, finLimit: BigDecimal, tradeLimit: BigDecimal)
    case class ResponseCard(account: String, cardNumber: String, accName: String, accType: String,
                            currency: String, cardType: String, mainCardNumber: String,
                            cardStat: String, src: String)
  }

}
