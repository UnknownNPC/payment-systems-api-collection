package com.github.unknownnpc.psw.qiwi.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.qiwi.model.QiwiModel.{AccountBalance, WalletHistory}
import org.apache.http.client.methods.HttpGet

private[qiwi] object QiwiSerializer {

  implicit val walletHistoryReqResSerializer: Serializer[WalletHistory.Request, WalletHistory.Response, HttpGet, String] = new WalletHistoryReqResSerializer
  implicit val accountBalanceReqResSerializer: Serializer[AccountBalance.Request, AccountBalance.Response, HttpGet, String] = new AccountBalanceReqResSerializer

}
