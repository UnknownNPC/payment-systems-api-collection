package com.github.unknownnpc.psw.p24.model

import java.util.Date

case class CardBalanceResponse(merchant: Merchant, data: CardBalanceResponseData)
case class CardBalanceResponseData(oper: String, cardBalance: CardBalanceResponseCardBalance, card: CardBalanceResponseCard)
case class CardBalanceResponseCardBalance(avBalance: BigDecimal, balDate: Date, balDyn: String,
                                          balance: BigDecimal, finLimit: BigDecimal, tradeLimit: BigDecimal)
case class CardBalanceResponseCard(account: String, cardNumber: String, accName: String, accType: String,
                                   currency: String, cardType: String, mainCardNumber: String,
                                   cardStat: String, src: String)
