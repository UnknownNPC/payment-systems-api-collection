package com.github.unknownnpc.psw.p24.model

import java.util.Date

case class WalletHistoryResponse(merchant: Merchant, data: WalletHistoryResponseData)
case class WalletHistoryResponseData(oper: String, info: WalletHistoryResponseInfo)
case class WalletHistoryResponseInfo(status: String, credit: String, debet: String, statements: List[WalletHistoryResponseStatementEntity])
case class WalletHistoryResponseStatementEntity(card: String, appcode: String, trandate: Date, amount: String, cardamount: String, rest: String, terminal: String, description: String)
