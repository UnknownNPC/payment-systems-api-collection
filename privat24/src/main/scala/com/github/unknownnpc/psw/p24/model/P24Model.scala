package com.github.unknownnpc.psw.p24.model

import java.util.Date

private[p24] object P24Model {

  case class Merchant(id: Long, signature: Option[String])

  object WalletHistory {

    case class WalletHistoryResponse(merchant: Merchant, data: WalletHistoryResponseData)
    case class WalletHistoryResponseData(oper: String, info: WalletHistoryResponseInfo)
    case class WalletHistoryResponseInfo(status: String, credit: String, debet: String, statements: List[WalletHistoryResponseStatementEntity])
    case class WalletHistoryResponseStatementEntity(card: String, appcode: String, trandate: Date, amount: String, cardamount: String, rest: String, terminal: String, description: String)

    case class WalletHistoryRequest(merchantPassword: String, merchant: Merchant, data: WalletHistoryRequestData)
    case class WalletHistoryRequestData(oper: String = "cmt", waitField: Long = 0, test: Long = 0, payment: WalletHistoryRequestDataPayment)
    case class WalletHistoryRequestDataPayment(idAttr: String = "", props: List[WalletHistoryRequestDataProp])
    case class WalletHistoryRequestDataProp(name: String, value: String)

  }


}
