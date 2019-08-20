package com.github.unknownnpc.psw.qiwi.model

trait AccountBalance

case class AccountBalanceRequest(apiToken: String, wallet: String)

case class AccountBalanceResponse(accounts: List[AccountBalanceResponseAccount])
case class AccountBalanceResponseAccount(alias: String, fsAlias: String, bankAlias: String,
                                         title: String, `type`: AccountBalanceResponseAccountType, hasBalance: Boolean, balance: AccountBalanceResponseAccountBalance,
                                         currency: Int, defaultAccount: Boolean)
case class AccountBalanceResponseAccountType(id: String, title: String)
case class AccountBalanceResponseAccountBalance(amount: BigDecimal, currency: Int)
