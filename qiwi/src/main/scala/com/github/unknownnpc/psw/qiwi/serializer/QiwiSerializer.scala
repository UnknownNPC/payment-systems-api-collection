package com.github.unknownnpc.psw.qiwi.serializer

import com.github.unknownnpc.psw.api.Serializer
import com.github.unknownnpc.psw.qiwi.model.{AccountBalanceRequest, AccountBalanceResponse, WalletHistoryRequest, WalletHistoryResponse}
import org.apache.http.client.methods.HttpGet

private[qiwi] object QiwiSerializer {

  implicit val walletHistoryReqResSerializer: Serializer[WalletHistoryRequest, WalletHistoryResponse, HttpGet, String] = new WalletHistoryReqResSerializer
  implicit val accountBalanceReqResSerializer: Serializer[AccountBalanceRequest, AccountBalanceResponse, HttpGet, String] = new AccountBalanceReqResSerializer

}
